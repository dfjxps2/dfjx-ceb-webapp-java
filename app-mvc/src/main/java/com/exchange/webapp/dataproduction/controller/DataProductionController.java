package com.exchange.webapp.dataproduction.controller;


import com.exchange.webapp.applicationmanagement.bean.AppProjectManagement;
import com.exchange.webapp.dataproduction.bean.DataProduction;
import com.exchange.webapp.dataproduction.service.DataProductionService;
import com.exchange.webapp.util.UrlPython;
import com.webapp.support.httpClient.HttpClientSupport;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 */
@Controller
@RequestMapping("/production")
public class DataProductionController {

    @Autowired
    private DataProductionService dataProductionService;


    //数据生产管理列表
    @RequestMapping("/dataproductionList")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataproductionList(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("prj_cd")String prj_cd,
            @RequestParam("prod_nm")String prod_nm
    ){
        PageResult pageResult = null;
        try{
            pageResult = dataProductionService.dataproductionList(currPage,pageSize,prj_cd,prod_nm);
        }catch(Exception e){
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "查询数据生产管理列表有误", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "查询数据生产管理列表成功", null, pageResult);
    }



    //查看
    @RequestMapping("/dataproductionselect")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataproductionselect(
            @RequestParam("prod_id") String prod_id){
        List<DataProduction> contactPageDatas;
        if ("".equals(prod_id)){
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "ID为空", null, "error");
        }
        try{
            contactPageDatas = dataProductionService.dataproductionselect(prod_id);
        }catch(Exception e){
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据查看失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据查看成功", null, contactPageDatas);
    }


    //查看数据消费方
    @RequestMapping("/dataproductionselectxff")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataproductionselectxff(
            @RequestParam("prod_id") String prod_id){
        List<DataProduction> contactPageDatas;
        try{
            contactPageDatas = dataProductionService.dataproductionselectxff(prod_id);
        }catch(Exception e){
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据消费方查看失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据消费方查看成功", null, contactPageDatas);
    }


    //新增生产
    @RequestMapping("/insertdataproduction")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertdataproduction(
            @RequestParam("prod_nm") String prod_nm,
            @RequestParam("dat_cd")String dat_cd,
            @RequestParam("prj_cd")String prj_cd,
            @RequestParam("create_cron")String create_cron,
            @RequestParam("upload_cron")String upload_cron,
            @RequestParam("storage_path")String storage_path,
            @RequestParam("flag")String flag){
        if(!prod_nm.isEmpty() && !dat_cd.isEmpty() && !prj_cd.isEmpty() && !create_cron.isEmpty() && !upload_cron.isEmpty() && !storage_path.isEmpty() ){
            try{
                //String data  = "* 0/1 7-23 * * ?";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CronExpression cronExpression = new CronExpression(create_cron);
                boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
            }catch(Exception e){
                return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请使用正确的cron表达式", null, "error");
            }
            try{
                //String data  = "* 0/1 7-23 * * ?";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CronExpression cronExpression = new CronExpression(upload_cron);
                boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
            }catch(Exception e){
                return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请使用正确的cron表达式", null, "error");
            }

/*
            int ss = 0;
            ss =   dataProductionService.yanzhengpath(storage_path);
            if(ss == 0){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "存储路径有误！", null, "error");
            }*/
            try{
                dataProductionService.insertdataproduction(prod_nm,dat_cd,prj_cd,create_cron,upload_cron,storage_path,flag);
                int projid  = dataProductionService.selectmaxprojid();
                Map<String,Object> params = new HashMap();
                String pythonHost = UrlPython.PYTHONHOST;
                HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);
                params.put("id",projid);
                httpClientSupport.sendRequest("/config/product/update",params, RequestMethod.POST,true);
             }catch(Exception e){
                return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增数据生产失败", null, "error");
            }
        }else{
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增数据生产成功", null, "success");
    }


    //修改
    @RequestMapping("/updatedataproduction")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatedataproduction(
            @RequestParam("prod_id") int prod_id,
            @RequestParam("prod_nm") String prod_nm,
            @RequestParam("dat_cd")String dat_cd,
            @RequestParam("prj_cd")String prj_cd,
            @RequestParam("create_cron")String create_cron,
            @RequestParam("upload_cron")String upload_cron,
            @RequestParam("storage_path")String storage_path,
            @RequestParam("flag")String flag){
        if(!prod_nm.isEmpty() && !dat_cd.isEmpty() && !prj_cd.isEmpty() && !create_cron.isEmpty() && !upload_cron.isEmpty() && !storage_path.isEmpty()){
            try{
                //String data  = "* 0/1 7-23 * * ?";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CronExpression cronExpression = new CronExpression(create_cron);
                boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
            }catch(Exception e){
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请使用正确的cron表达式", null, "error");
            }
            try{
                //String data  = "* 0/1 7-23 * * ?";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CronExpression cronExpression = new CronExpression(upload_cron);
                boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
            }catch(Exception e){
                return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请使用正确的cron表达式", null, "error");
            }


          /*  int ss = 0;
            ss =   dataProductionService.yanzhengpath(storage_path);
            if(ss == 0){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "存储路径有误！", null, "error");
            }*/
            try{
                dataProductionService.updatedataproduction(prod_id,prod_nm,dat_cd,prj_cd,create_cron,upload_cron,storage_path,flag);
                Map<String,Object> params = new HashMap();
                String pythonHost = UrlPython.PYTHONHOST;
                HttpClientSupport httpClientSupport = HttpClientSupport.getInstance(pythonHost);
                params.put("id",prod_id);
                httpClientSupport.sendRequest("/config/product/update",params, RequestMethod.POST,true);
            }catch(Exception e){
                return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据生产修改失败", null, "error");
            }
        }else{
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据生产修改成功", null, "success");
    }



    //修改状态
    @RequestMapping("/delproduction")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String delproduction(
            @RequestParam("prod_id") int prod_id,
            @RequestParam("flag")int flag
            ){
        try{
            dataProductionService.delproduction(prod_id,flag);
        }catch(Exception e){
            return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改失败", null, "error");
        }
        return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改成功", null, "success");
    }


    //数据生产项目下拉
    @RequestMapping("/dataProductionprojrctlist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataProductionprojrctlist(){
        List<AppProjectManagement> contactPageDatas;
        try{
            contactPageDatas = dataProductionService.dataProductionprojrctlist();
        }catch(Exception e){
            return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "项目列表失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "成功", null, contactPageDatas);
    }




    @RequestMapping("/cron")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public static String cron(String data) throws Exception {
        try{
            //String data  = "* 0/1 7-23 * * ?";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CronExpression cronExpression = new CronExpression(data);
            boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2018-04-27 16:00:00"));
        }catch(Exception e){
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请使用正确的cron表达式", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "cron表达式正确", null, "success");

    }


}
