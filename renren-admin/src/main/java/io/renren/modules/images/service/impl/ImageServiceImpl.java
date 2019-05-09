package io.renren.modules.images.service.impl;

import io.renren.common.dao.DaoSupport;
import io.renren.common.entity.Page;
import io.renren.common.entity.PageData;
import io.renren.common.util.DateUtil;
import io.renren.modules.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private DaoSupport daoSupport;

    @Override
    public List<PageData> getList(Page page) throws Exception {
        return (List<PageData>)daoSupport.findForList("ImageDao.imagelistPage",page);
    }

    @Override
    public PageData save(PageData pageData, MultipartFile picture, HttpServletRequest request) throws Exception {
        //获取文件名和保存的文件
        String fileName = (String) this.uploadImage(picture,request).get("fileName");
        File targetFile = (File) this.uploadImage(picture,request).get("targetFile");

        //将文件保存到服务器指定位置
        picture.transferTo(targetFile);
        System.out.println("上传成功");
        //将路径、文件名保存到数据库
        pageData.put("url","/upload/"+fileName);
        pageData.put("imagename",fileName);
        pageData.put("createtime", DateUtil.getTime());
        daoSupport.save("ImageDao.save", pageData);
        System.out.println(pageData);
        return pageData;
    }

    @Override
    public Map<String, Object> save(PageData pageData, MultipartFile[] picture, HttpServletRequest request) throws Exception {
        Map<String ,Object> pageDataMap = new HashMap<>();

        for (int i=0; i<picture.length; i++){
            //获取文件名和保存的文件
            String fileName = (String) this.uploadImage(picture[i],request).get("fileName");
            File targetFile = (File) this.uploadImage(picture[i],request).get("targetFile");

            //将文件保存到服务器指定位置
            picture[i].transferTo(targetFile);
            System.out.println("上传成功");
            //将路径、文件名保存到数据库
            pageData.put("url","/upload/"+fileName);
            pageData.put("imagename",fileName);
            pageData.put("createtime", DateUtil.getTime());
            System.out.println(pageData.toString());
            daoSupport.save("ImageDao.save",pageData);
            //存放到pageDataList中返回
            pageDataMap.put(Integer.toString(i),pageData);
        }
        return pageDataMap;
    }

    /**
     * 共用方法
     * @param picture
     * @param request
     * @return
     */
    private Map<String ,Object> uploadImage(MultipartFile picture, HttpServletRequest request){
        //获取文件在服务器的储存位置
        String path = request.getSession().getServletContext().getRealPath("/upload");
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = picture.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(d);
        String fileName = date + name + "." + type;
        System.out.println("新文件名称：" + fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);

        Map<String ,Object> map = new HashMap<>();
        map.put("targetFile",targetFile);
        map.put("fileName",fileName);
        return map;
    }

    @Override
    public void delImage(PageData pageData, HttpServletRequest request) throws Exception {
        //封装参数
        String path = request.getSession().getServletContext().getRealPath("/upload/");
        pageData.put("filePath", path + pageData.get("imagename"));
        pageData.put("id", Integer.parseInt(pageData.get("id").toString()));
        //删除实际的文件
        File file = new File((String) pageData.get("filePath"));
        if (file.delete()) {
            //删除数据库中的字段
            daoSupport.delete("ImageDao.delete",pageData);
            return;
        }else {
            throw new Exception("文件删除失败");
        }
    }

}
