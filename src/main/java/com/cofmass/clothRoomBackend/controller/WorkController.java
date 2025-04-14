package com.cofmass.clothRoomBackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cofmass.clothRoomBackend.entity.*;
import com.cofmass.clothRoomBackend.mapper.WorkMapper;
import com.cofmass.clothRoomBackend.service.*;
import com.cofmass.clothRoomBackend.utils.R;
import com.cofmass.clothRoomBackend.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cofmass.clothRoomBackend.utils.ImageLocalUrl.file2Url;
import static com.cofmass.clothRoomBackend.utils.ImageToBase64Util.convertFileToBase64;

@RestController
@RequestMapping("/work")
@Api(tags = "作品管理")
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LikeTbService likeService;
    @Autowired
    private CollectionsService collectionsService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private CommentLikeService commentLikeService;
    @Autowired
    private WorkMapper workMapper;


    @PostMapping("/add")
    @ApiOperation("添加作品")
    public R addWork(@RequestBody WorkVo work){
        User loginUser = userService.getLoginUser();
        work.setUserId(loginUser.getUserId());
        User byId = userService.getById(loginUser.getUserId());
        work.setUserName(byId.getUserName());
        work.setAvatarImg(byId.getAvatarUrl());
        if (workService.save(work)){
            return R.ok("添加成功");
        }
        return R.error("添加失败");
    }

    @GetMapping("/like")
    @ApiOperation("点赞作品")
    public R like(@RequestParam String workId){
        String likeId = "LK"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        User loginUser = userService.getLoginUser();
        likeService.save(new LikeTb(likeId,workId,loginUser.getUserId(),null));
        return R.ok("点赞成功");
    }

    @GetMapping("/dislike")
    @ApiOperation("取消点赞作品")
    public R dislike(@RequestParam String workId){
        User loginUser = userService.getLoginUser();
        likeService.remove(new LambdaQueryWrapper<LikeTb>().eq(LikeTb::getWorkId,workId).eq(LikeTb::getUserId,loginUser.getUserId()));
        return R.ok("取消点赞成功");
    }

    @GetMapping("/isLike")
    @ApiOperation("是否点赞作品")
    public R isLike(@RequestParam String workId){
        User loginUser = userService.getLoginUser();
        LikeTb one = likeService.getOne(new LambdaQueryWrapper<LikeTb>().eq(LikeTb::getWorkId, workId).eq(LikeTb::getUserId, loginUser.getUserId()));
        if (one != null){
            return R.ok("用户点赞了~").put("data",true);
        }else {
            return R.ok("用户没有点赞~").put("data",false);
        }
    }

    @GetMapping("/likeNum")
    @ApiOperation("作品点赞数量")
    public R likeNum(@RequestParam String workId){
         long count = likeService.count(new LambdaQueryWrapper<LikeTb>().eq(LikeTb::getWorkId, workId));
         return R.ok("查询成功").put("data",count);
     }

    @GetMapping("/list")
    @ApiOperation("展示作品列表")
    public R list(){
        List<Work> list = workService.list();
        ArrayList<WorkVo> workVos = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            for (Work w :list) {
                List<String> images = workService.getImagesById(w.getWorkId());
                WorkVo workVo = new WorkVo();
                BeanUtils.copyProperties(w,workVo);
                workVo.setImages(images);
                workVos.add(workVo);
                workVo.setIndexImg(images.get(0));
                workVo.setAvatarImg(convertFileToBase64(w.getAvatarImg()));
            }
            return R.ok("查询成功").put("data",workVos);
        }
        return R.error("查询失败");
    }

     @GetMapping("/myList")
    @ApiOperation("展示我的作品列表")
    public R myList(){
        String userId = userService.getLoginUser().getUserId();
        List<Work> list = workService.list(new LambdaQueryWrapper<Work>().eq(Work::getUserId,userId));
        ArrayList<WorkVo> workVos = new ArrayList<>();

        if (list != null && !list.isEmpty()){
            for (Work w :list) {
                List<String> images = workService.getImagesById(w.getWorkId());
                WorkVo workVo = new WorkVo();
                BeanUtils.copyProperties(w,workVo);
                workVo.setImages(images);
                workVos.add(workVo);
                workVo.setIndexImg(images.get(0));
                workVo.setAvatarImg(convertFileToBase64(w.getAvatarImg()));
            }
            return R.ok("查询成功").put("data",workVos);
        }
        return R.error("查询失败");
    }

    @GetMapping("/getOne")
    @ApiOperation("根据作品ID获取作品详情")
    public R getOne(@RequestParam String workId){
        Work w = workService.getById(workId);
        List<String> images = workService.getImagesById(workId);
        WorkDetailVo work = new WorkDetailVo();
        BeanUtils.copyProperties(w,work);
        work.setImages(images);
        work.setAvatarImg(convertFileToBase64(work.getAvatarImg()));
        return R.ok("查询成功").put("data",work);
    }

    @PostMapping("/upload/{workId}")
    @ApiOperation("上传图片")
    public R uploadImg(MultipartFile file,@PathVariable String workId){
//        传入workId这里不需要管什么，就给我存进image先
        try {
            String newName = file2Url(file);
            if (file.isEmpty()){
                return R.error("传入了空的文件");
            }
            file.transferTo(Paths.get(newName));
            imageService.save(new Image(workId,newName));
            return R.ok("上传成功").put("imgUrl",newName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/favor")
    @ApiOperation("收藏作品")
    public R favor(@RequestParam String workId){
        String favorId = "FV"+ new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        User loginUser = userService.getLoginUser();
        collectionsService.save(new Collections(favorId,workId,loginUser.getUserId(),null));
        return R.ok("收藏成功");
    }

    @GetMapping("/disfavor")
    @ApiOperation("取消收藏作品")
    public R disfavor(@RequestParam String workId){
        User loginUser = userService.getLoginUser();
        collectionsService.remove(new LambdaQueryWrapper<Collections>().eq(Collections::getWorkId,workId).eq(Collections::getUserId,loginUser.getUserId()));
        return R.ok("取消收藏成功");
    }

    @GetMapping("/isFavor")
    @ApiOperation("是否收藏作品")
    public R isFavor(@RequestParam String workId){
        User loginUser = userService.getLoginUser();
        Collections one = collectionsService.getOne(new LambdaQueryWrapper<Collections>().eq(Collections::getWorkId, workId).eq(Collections::getUserId, loginUser.getUserId()));
        if (one != null){
            return R.ok("用户收藏了~").put("data",true);
        }else {
            return R.ok("用户没有收藏~").put("data",false);
        }
    }

    @GetMapping("/favorNum")
    @ApiOperation("作品收藏数量")
    public R favorNum(@RequestParam String workId){
         long count = collectionsService.count(new LambdaQueryWrapper<Collections>().eq(Collections::getWorkId, workId));
         return R.ok("查询成功").put("data",count);
    }

    @GetMapping("/favorWorkList")
    @ApiOperation("收藏作品列表")
    public R favorWorkList(){
        User loginUser = userService.getLoginUser();
        List<Collections> list = collectionsService.list(new LambdaQueryWrapper<Collections>()
                .eq(Collections::getUserId, loginUser.getUserId()));
        ArrayList<WorkVo> workVos = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            for (Collections o :list) {
                Work byId = workService.getById(o.getWorkId());
                List<String> images = workService.getImagesById(o.getWorkId());
                WorkVo workVo = new WorkVo();
                BeanUtils.copyProperties(byId,workVo);
                workVo.setImages(images);
                workVo.setIndexImg(images.get(0));
                workVos.add(workVo);
            }
            return R.ok("查询成功").put("data",workVos);
        }
        return R.error("查询失败");
    }

    @GetMapping("/likeWorkList")
    @ApiOperation("点赞作品列表")
    public R likeWorkList(){
        User loginUser = userService.getLoginUser();
        List<LikeTb> list = likeService.list(new LambdaQueryWrapper<LikeTb>()
                .eq(LikeTb::getUserId, loginUser.getUserId()));
        ArrayList<WorkVo> workVos = new ArrayList<>();
        if (list != null && !list.isEmpty()){
            for (LikeTb o :list) {
                Work byId = workService.getById(o.getWorkId());
                List<String> images = workService.getImagesById(o.getWorkId());
                WorkVo workVo = new WorkVo();
                BeanUtils.copyProperties(byId,workVo);
                workVo.setImages(images);
                workVo.setIndexImg(images.get(0));
                workVos.add(workVo);
            }
            return R.ok("查询成功").put("data",workVos);

        }
        return R.error("查询失败");

    }

    @GetMapping("/commentsWorkList")
    @ApiOperation("评论作品列表")
    public R commentsWorkList(){
        User loginUser = userService.getLoginUser();
//        根据用户查出当前用户的所有评论
        List<Comments> list = commentsService.list(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getUserId, loginUser.getUserId()));
        ArrayList<CommentsWorkVo> vos = new ArrayList<>();
        if (list!=null && !list.isEmpty()){
            for (Comments c :list) {
            String workId = c.getWorkId();
            Work work = workService.getById(workId);
            List<String> images = workService.getImagesById(workId);
            CommentsWorkVo commentsWorkVo = new CommentsWorkVo();
            BeanUtils.copyProperties(c,commentsWorkVo);
            commentsWorkVo.setWorkTitle(work.getWorkTitle());
            commentsWorkVo.setIndexImg(images.get(0));
            long likeNum = commentLikeService.count(new LambdaQueryWrapper<CommentLike>()
                    .eq(CommentLike::getCommentId, c.getCommentId()));
            commentsWorkVo.setLikeNum(likeNum);
            vos.add(commentsWorkVo);
        }
        return R.ok("查询成功").put("data",vos);
        }
        return R.error("查询失败");
    }


    @PostMapping("/backWorkList")
    @ApiOperation("管理后端所有作品列表")
    public R backWorkList(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "8") int pageSize, @RequestBody(required = false) WorkSearchVo param){
        Page<Work> workPage = workService.pageWork(currentPage, pageSize,param);
        List<Work> records = workPage.getRecords();
        for (Work work :records) {
            work.setAvatarImg(convertFileToBase64(work.getAvatarImg()));
            work.setIndexImg(convertFileToBase64(work.getIndexImg()));
        }
        workPage.setRecords(records);
        return R.ok("查询成功").put("data",workPage);
    }

    @GetMapping("/getNewWorkNum")
    @ApiOperation("获取今日新增作品数量")
    public R getNewWorkNum(){
        int newWorkNum = workMapper.getNewWorkNum();
        return R.ok("查询成功").put("data",newWorkNum);
    }

    @ApiOperation("修改作品")
    @PutMapping("/update")
    public R updateById(@RequestBody Work work){
        workService.updateById(work);
        return R.ok("修改成功");
    }

    @ApiOperation("修改作品")
    @DeleteMapping("/delete")
    public R deleteById(@RequestParam String workId){
        workService.removeById(workId);
        return R.ok("删除成功");
    }



}
