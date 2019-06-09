package com.gzpzg.gamesearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzpzg.gamesearch.Entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
public class controller {

    static HttpUtil httpUtil = new HttpUtil();
    static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @RequestMapping(value = "/data",method = RequestMethod.POST)
    public String data(@RequestBody Data data) throws IOException {
        if(data==null||data.getGremlin()==null){
            return "{}";
        }
        String result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\""+data.getGremlin()+"\"}");
        result = removeStr(result);
        return result;
    }


    public static String removeStr(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<str.length();i++){
            char c = str.charAt(i);
            if(c != '@'){
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
    @RequestMapping(value = "/getNode",method = RequestMethod.GET)
    public String getNode(@RequestParam("name")String name) throws Exception {
        if(name==null){
            throw new Exception("异常");
        }
        String result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V().has('name','"+name+"')\"}");
        result = removeStr(result);
        VertexData data = objectMapper.readValue(result,VertexData.class);
        if(data.getStatus().getCode()==200&&data.getResult().getData().getValue().size()!=0){
            Map<String,Object> resultData = new HashMap<>();//返回的数据集，包含节点集与节点信息集
            List<VertexValue> vertexs = data.getResult().getData().getValue();//待处理的节点集
            List<NodeData> nodeData = new ArrayList<>();//返回的节点集
            List<NodeData> relationship = new ArrayList<>();//返回的节点集
            solveVertexs(vertexs,resultData,nodeData,relationship,0);
            resultData.put("node",nodeData);
            resultData.put("relationship",relationship);
            return objectMapper.writeValueAsString(resultData);
        }else{
            throw new Exception("异常");
        }
    }

    public List<NodeData> solveVertexs(List<VertexValue> vertexs,Map<String,Object> resultData,List<NodeData> nodeDatas,List<NodeData> relationship,int time) throws IOException {
        List<NodeData> thisResultNodeDatas = new ArrayList<>();
        for(VertexValue vertex:vertexs){
            String id = vertex.getValue().getId().getValue().toString();//节点ID
            Map<String,List<VertexPropertie>> vertexInfoOld = vertex.getValue().getProperties();
            String type = vertexInfoOld.get("type").get(0).getValue().getValue().toString();
            NodeData vertexNodeData = null;
            vertexNodeData = new NodeData(id,vertexInfoOld.get("name").get(0).getValue().getValue().toString());
            thisResultNodeDatas.add(vertexNodeData);//添加到当前节点集中
            if(resultData.containsKey(id))continue;//已处理过的节点
            Map<String,Object> vertexInfo = new HashMap<>();//节点属性提取
            for(String key:vertexInfoOld.keySet()){//实体属性集提取
                Object object = vertexInfoOld.get(key).get(0).getValue().getValue();
                if(object instanceof String){
                    vertexInfo.put(key,object);
                }else{
                    vertexInfo.put(key,((Map<String,Object>)object).get("value"));
                }
            }
            //设置颜色
            if(time==0){
                vertexNodeData.setColor("#FF0000");
            }else{
                switch (type){
                    case "skill":
                        vertexNodeData.setColor("#66CDAA");
                        break;
                    case "hero":
                        vertexNodeData.setColor("#A020F0");
                        break;
                    case "equip":
                        vertexNodeData.setColor("#0000CD");
                        break;
                    default:break;
                }
            }
            nodeDatas.add(vertexNodeData);//添加到全局节点集中
            //结果集中放入该节点属性信息
            resultData.put(id,vertexInfo);
            if(time!=0)continue;//后续节点不进行查询关系，只对最初查询到的实体查询关系
            if(vertexInfoOld.containsKey("type")){
                String vertexType = vertexInfoOld.get("type").get(0).getValue().getValue().toString().trim();
                switch (vertexType){
                    case "hero":
                        //查询英雄技能
                        solveEdge(id,"has","英雄技能",resultData,nodeDatas,relationship,true,1);
                        //查询英雄适合装备
                        solveEdge(id,"fit","推荐装备",resultData,nodeDatas,relationship,true,1);
                        //查询英雄的压制英雄
                        solveEdge(id,"压制英雄","压制",resultData,nodeDatas,relationship,true,1);
                        //查询英雄的被压制英雄
                        solveEdge(id,"被压制英雄","被压制",resultData,nodeDatas,relationship,true,1);
                        //查询英雄的最佳拍档
                        solveEdge(id,"最佳拍档","拍档英雄",resultData,nodeDatas,relationship,true,1);
                        break;
                    case "skill":
                        //查询技能归属的英雄
                        solveEdge(id,"has","技能属于",resultData,nodeDatas,relationship,false,1);
                        break;
                    case "equip":
                        //查询用什么装备合成
                        solveEdge(id,"need","合成需要",resultData,nodeDatas,relationship,false,1);
                        //查询可以合成什么装备
                        solveEdge(id,"need","可以合成",resultData,nodeDatas,relationship,true,1);
                        //查询适合谁
                        solveEdge(id,"fit","适合出装",resultData,nodeDatas,relationship,false,1);
                        break;
                    default: break;
                }
            }
        }
        return thisResultNodeDatas;
    }

    //处理节点的边关系
    public void solveEdge(String id,String edgeName,String edgeShowName,Map<String,Object> resultData,List<NodeData> nodeDatas,List<NodeData> relationship,boolean isOut,int time) throws IOException {
        String result = null;
        if(isOut){
            result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V("+id+").out('"+edgeName+"')\"}");
        }else{
            result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V("+id+").in('"+edgeName+"')\"}");
        }
        result = removeStr(result);
        VertexData vrtexData = objectMapper.readValue(result,VertexData.class);
        if(vrtexData.getStatus().getCode()==200&&vrtexData.getResult().getData().getValue().size()!=0){
            List<NodeData> heroEquips = solveVertexs(vrtexData.getResult().getData().getValue(),resultData,nodeDatas,relationship,1);
            for(NodeData nodeData:heroEquips){
                relationship.add(new NodeData(edgeShowName,id,nodeData.getUuid()));
            }
        }
    }

/**
    @RequestMapping(value = "/getAllNode",method = RequestMethod.GET)
    public String getAllNode(@RequestParam("type")String type) throws Exception {
        if(type==null){
            throw new Exception("异常");
        }
        String result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V().has('type','"+type+"')\"}");
        result = removeStr(result);
        VertexData data = objectMapper.readValue(result,VertexData.class);
        if(data.getStatus().getCode()==200&&data.getResult().getData().getValue().size()!=0){
            Map<String,Object> resultData = new HashMap<>();//返回的数据集，包含节点集与节点信息集
            List<VertexValue> vertexs = data.getResult().getData().getValue();//待处理的节点集
            List<NodeData> nodeData = new ArrayList<>();//返回的节点集
            List<NodeData> relationship = new ArrayList<>();//返回的节点集
            solveVertexs2(vertexs,resultData,nodeData,relationship,type);
            resultData.put("node",nodeData);
            resultData.put("relationship",relationship);
            return objectMapper.writeValueAsString(resultData);
        }else{
            throw new Exception("异常");
        }
    }
    //测试函数
    public List<NodeData> solveVertexs2(List<VertexValue> vertexs,Map<String,Object> resultData,List<NodeData> nodeDatas,List<NodeData> relationship,String type) throws IOException {
        List<NodeData> thisResultNodeDatas = new ArrayList<>();
        for(VertexValue vertex:vertexs){
            String id = vertex.getValue().getId().getValue().toString();//节点ID
            Map<String,List<VertexPropertie>> vertexInfoOld = vertex.getValue().getProperties();
            NodeData vertexNodeData = null;
            vertexNodeData = new NodeData(id,vertexInfoOld.get("name").get(0).getValue().getValue().toString());
            thisResultNodeDatas.add(vertexNodeData);//添加到当前节点集中
            if(resultData.containsKey(id))continue;//已处理过的节点
            Map<String,Object> vertexInfo = new HashMap<>();//节点属性提取
            //设置颜色
            switch (type){
                case "skill":
                    vertexNodeData.setColor("#66CDAA");
                    break;
                case "hero":
                    vertexNodeData.setColor("#A020F0");
                    break;
                case "equip":
                    vertexNodeData.setColor("#0000CD");
                    break;
                default:break;
            }
            nodeDatas.add(vertexNodeData);//添加到全局节点集中
            //结果集中放入该节点属性信息
            resultData.put(id,vertexInfo);
            if(vertexInfoOld.containsKey("type")){
                String vertexType = vertexInfoOld.get("type").get(0).getValue().getValue().toString().trim();
                switch (vertexType){
                    case "hero":
                        //查询英雄的压制英雄
                        solveEdge2(id,"压制英雄","压制",resultData,nodeDatas,relationship,true,type);
                        //查询英雄的被压制英雄
                        solveEdge2(id,"被压制英雄","被压制",resultData,nodeDatas,relationship,true,type);
                        //查询英雄的最佳拍档
                        solveEdge2(id,"最佳拍档","拍档英雄",resultData,nodeDatas,relationship,true,type);
                        break;
                    case "equip":
                        //查询用什么装备合成
                        solveEdge2(id,"need","合成需要",resultData,nodeDatas,relationship,false,type);
                        //查询可以合成什么装备
                        solveEdge2(id,"need","可以合成",resultData,nodeDatas,relationship,true,type);
                        break;
                    default: break;
                }
            }
        }
        return thisResultNodeDatas;
    }

    //处理节点的边关系
    public void solveEdge2(String id,String edgeName,String edgeShowName,Map<String,Object> resultData,List<NodeData> nodeDatas,List<NodeData> relationship,boolean isOut,String type) throws IOException {
        String result = null;
        if(isOut){
            result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V("+id+").out('"+edgeName+"')\"}");
        }else{
            result = httpUtil.post("http://106.14.5.35:8182","{\"gremlin\":\"g.V("+id+").in('"+edgeName+"')\"}");
        }
        result = removeStr(result);
        VertexData vrtexData = objectMapper.readValue(result,VertexData.class);
        if(vrtexData.getStatus().getCode()==200&&vrtexData.getResult().getData().getValue().size()!=0){
            List<NodeData> heroEquips = solveVertexs2(vrtexData.getResult().getData().getValue(),resultData,nodeDatas,relationship,type);
            for(NodeData nodeData:heroEquips){
                relationship.add(new NodeData(edgeShowName,id,nodeData.getUuid()));
            }
        }
    }
**/
}
