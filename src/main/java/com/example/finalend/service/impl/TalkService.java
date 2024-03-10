package com.example.finalend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.finalend.annotation.HistoryLog;
import com.example.finalend.entity.Follow;
import com.example.finalend.entity.Talk;
import com.example.finalend.entity.User;
import com.example.finalend.exception.ApiException;
import com.example.finalend.mapper.*;
import com.example.finalend.param.TalkParam;
import com.example.finalend.relation.TalkImg;
import com.example.finalend.relation.TalkLove;
import com.example.finalend.relation.TalkTag;
import com.example.finalend.service.ITalkService;
import com.example.finalend.utils.RedisUtil;
import com.example.finalend.vo.PageVo;
import com.example.finalend.vo.TalkVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TalkService extends ServiceImpl<TalkMapper, Talk> implements ITalkService  {

    @Resource
    UserMapper userMapper;

    @Resource
    TalkMapper talkMapper;

    @Resource
    TalkImgMapper talkImgMapper;

    @Resource
    TalkTagMapper talkTagMapper;

    @Resource
    TalkLoveMapper talkLoveMapper;

    @Resource
    FollowMapper followMapper;

    @Resource
    FollowService followService;

    @Resource
    RedisUtil redisUtil;

    @Override
    @Transactional
    public boolean saveTalk(TalkParam talkParam) {
        if (talkParam == null || StrUtil.isBlank(talkParam.getContent()) || talkParam.getImages() == null || talkParam.getTags() == null) {
            throw new ApiException("参数错误");
        }
        Talk talk = new Talk();
        talk.setContent(talkParam.getContent());
        talk.setPublishTime(LocalDateTime.now());
        talk.setUserId(StpUtil.getLoginIdAsLong());
        talk.setCommentNumber(0);
        talk.setLoveNumber(0);
        talk.setDeleted(false);
        save(talk);
        for (String image : talkParam.getImages()) {
            TalkImg talkImg = new TalkImg();
            talkImg.setTalkId(talk.getId());
            talkImg.setImage(image);
            talkImgMapper.insert(talkImg);
        }
        for (String tag : talkParam.getTags()) {
            TalkTag talkTag = new TalkTag();
            talkTag.setTalkId(talk.getId());
            talkTag.setTag(tag);
            talkTagMapper.insert(talkTag);
        }
        return true;
    }

    @Override
    @Transactional
    public List<TalkVo> getTalks(Long id) {
        long loginId = StpUtil.getLoginIdAsLong();
        List<Long> followIds = followService.getFollowIds(loginId);
        followIds.add(loginId);
        List<Talk> talks = list(new QueryWrapper<Talk>().lt(id != null, "id", id).in("user_id", followIds).orderByDesc("publish_time").last("limit 10"));
        return getTalkVos(talks, loginId);
    }

    @Override
    @Transactional
    public void addFollow(TalkParam talkParam) {
        if (talkParam == null) {
            throw new ApiException("参数不合理");
        }
        Talk talk = talkMapper.selectOne(new QueryWrapper<Talk>().select("user_id").eq("id", talkParam.getId()));
        if (talk == null) {
            throw new ApiException("参数不合理");
        }
        if (talk.getUserId() == StpUtil.getLoginIdAsLong()) {
            throw new ApiException("不可以关注自己");
        }
        Follow follow = new Follow();
        follow.setUserId(StpUtil.getLoginIdAsLong());
        follow.setFollowId(talk.getUserId());
        follow.setFollowTime(LocalDateTime.now());
        followMapper.insert(follow);
    }

    @Transactional
    public boolean loveTalk(TalkParam talkParam) {
        if (talkParam == null) {
            throw new ApiException("参数不合理");
        }
        boolean exists = talkLoveMapper.exists(new QueryWrapper<TalkLove>().eq("talk_id", talkParam.getId()).eq("user_id", StpUtil.getLoginIdAsLong()));
        if (exists) {
            update(new UpdateWrapper<Talk>().eq("id", talkParam.getId()).setSql("love_number = love_number - 1"));
            talkLoveMapper.delete(new QueryWrapper<TalkLove>().eq("talk_id", talkParam.getId()).eq("user_id", StpUtil.getLoginIdAsLong()));
            return false;
        } else {
            update(new UpdateWrapper<Talk>().eq("id", talkParam.getId()).setSql("love_number = love_number + 1"));
            TalkLove talkLove = new TalkLove();
            talkLove.setTalkId(talkParam.getId());
            talkLove.setUserId(StpUtil.getLoginIdAsLong());
            talkLoveMapper.insert(talkLove);
            return true;
        }
    }

    @Transactional
    public void delTalk(TalkParam talkParam) {
        Talk talk = talkMapper.selectOne(new QueryWrapper<Talk>().eq("id", talkParam.getId()));
        if (talk == null) {
            throw new ApiException("参数错误");
        }
        if (talk.getUserId() != StpUtil.getLoginIdAsLong()) {
            throw new ApiException("非本人动态，不可删除");
        }
        talkMapper.delete(new QueryWrapper<Talk>().eq("id", talkParam.getId()));
        talkLoveMapper.delete(new QueryWrapper<TalkLove>().eq("talk_id", talkParam.getId()));
        talkTagMapper.delete(new QueryWrapper<TalkTag>().eq("talk_id", talkParam.getId()));
    }

    public void subFollow(TalkParam talkParam) {
        if (talkParam == null) {
            throw new ApiException("参数不合理");
        }
        Talk talk = talkMapper.selectOne(new QueryWrapper<Talk>().select("user_id").eq("id", talkParam.getId()));
        if (talk == null) {
            throw new ApiException("参数不合理");
        }
        if (talk.getUserId() == StpUtil.getLoginIdAsLong()) {
            throw new ApiException("不可以取关自己");
        }
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<Follow>().eq("user_id", StpUtil.getLoginIdAsLong()).eq("follow_id", talk.getUserId());
        if (!followMapper.exists(queryWrapper)) {
            throw new ApiException("未关注不可取关");
        }
        followMapper.delete(queryWrapper);

    }

    public List<TalkVo> getHotTalk(Long id) {
        List<Talk> talks = list(new QueryWrapper<Talk>().lt(id != null, "id", id).orderByDesc("publish_time").last("limit 10"));
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId == null) {
            loginId = -1;
        }
        return getTalkVos(talks, Long.parseLong(loginId.toString()));
    }

    @HistoryLog(type = "talk")
    public TalkVo getSingleTalk(Long id) {
        if (id == null) {
            throw new ApiException("参数错误");
        }
        Talk talk = talkMapper.selectOne(new QueryWrapper<Talk>().eq("id", id));
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId == null) {
            loginId = -1;
        }
        return getTalkVo(talk, Long.parseLong(loginId.toString()));
    }

    private List<String> getImages(Talk talk) {
        List<TalkImg> talkImgs = talkImgMapper.selectList(new QueryWrapper<TalkImg>().select("image").eq("talk_id", talk.getId()));
        List<String> img = new ArrayList<>();
        for (TalkImg talkImg : talkImgs) {
            img.add(talkImg.getImage());
        }
        return img;
    }

    private List<String> getTags(Talk talk) {
        List<TalkTag> talkTags = talkTagMapper.selectList(new QueryWrapper<TalkTag>().select("tag").eq("talk_id", talk.getId()));
        List<String> tags = new ArrayList<>();
        for (TalkTag talkTag : talkTags) {
            tags.add(talkTag.getTag());
        }
        return tags;
    }

    private List<TalkVo> getTalkVos(List<Talk> talks, long loginId) {
        List<TalkVo> talkVos = new ArrayList<>();
        for (Talk talk : talks) {
            talkVos.add(getTalkVo(talk, loginId));
        }
        return talkVos;
    }

    private TalkVo getTalkVo(Talk talk, long loginId) {
        List<String> images = getImages(talk);
        List<String> tags = getTags(talk);
        boolean love = talkLoveMapper.exists(new QueryWrapper<TalkLove>().eq("talk_id", talk.getId()).eq("user_id", loginId));
        boolean self = talk.getUserId().equals(loginId);
        boolean follow = followMapper.exists(new QueryWrapper<Follow>().eq("user_id", loginId).eq("follow_id", talk.getUserId()));
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", talk.getUserId()));
        String name = user.getName();
        String avatar = user.getAvatar();
        return TalkVo.of(talk, love, self, follow, name, avatar, images, tags);
    }

    public PageVo<TalkVo> getSelfTalks(Integer pageNum, Integer pageSize) {
        long loginId = StpUtil.getLoginIdAsLong();
        Page<Talk> talkPage = talkMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<Talk>().eq("user_id", loginId).orderByDesc("publish_time"));
        List<Talk> records = talkPage.getRecords();
        List<TalkVo> talkVos = getTalkVos(records, loginId);
        return new PageVo<>(talkVos, talkPage.getSize(), talkPage.getCurrent(), talkPage.getTotal());
    }

    public void addCommentNumber(Long talkId) {
        update(new UpdateWrapper<Talk>().eq("id", talkId).setSql("comment_number = comment_number + 1"));
    }

    public List<TalkVo> getTalkByTag(Long id, String tag) {
        List<TalkTag> talkTags = talkTagMapper.selectList(new QueryWrapper<TalkTag>().eq("tag", tag));
        List<Long> ids = new ArrayList<>();
        for (TalkTag talkTag : talkTags) {
            ids.add(talkTag.getTalkId());
        }
        List<Talk> talks = list(new QueryWrapper<Talk>().lt(id != null, "id", id).in("id", ids).orderByDesc("publish_time").last("limit 10"));
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId == null) {
            loginId = -1;
        }
        return getTalkVos(talks, Long.parseLong(String.valueOf(loginId)));
    }
}
