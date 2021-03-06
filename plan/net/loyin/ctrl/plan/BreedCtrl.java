/**
 * 
 */
package net.loyin.ctrl.plan;

import java.util.HashMap;
import java.util.Map;

import net.loyin.ctrl.base.AdminBaseController;
import net.loyin.jfinal.anatation.PowerBind;
import net.loyin.jfinal.anatation.RouteBind;
import net.loyin.model.plan.Breed;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xiangning
 * 种植控制器
 */
@RouteBind(path="breed",sys="计划",model="养殖计划计划")
public class BreedCtrl extends AdminBaseController<Breed> {
	public BreedCtrl(){
		this.modelClass = Breed.class;
	}
	
	public void dataGrid(){
		Map<String,String>userMap=this.getUserMap();
		Map<String,Object> filter=new HashMap<String,Object>();
		filter.put("company_id",userMap.get("company_id"));
		filter.put("keyword",this.getPara("keyword"));
		filter.put("start_date",this.getPara("start_date"));
		filter.put("end_date",this.getPara("end_date"));
		filter.put("uid",this.getPara("uid"));
		filter.put("user_id",userMap.get("uid"));
		filter.put("position_id",userMap.get("position_id"));
		this.sortField(filter);
		this.rendJson(true, null, "",Breed.dao.pageGrid(this.getPageNo(),this.getPageSize(),filter));
	}
	
	@PowerBind(code="A1_1_E",funcName="编辑")
	public void save() {
		try {
			Breed po = (Breed) getModel();
			if (po == null) {
				this.rendJson(false,null, "提交数据错误！");
				return;
			}
			getId();
			String uid=this.getCurrentUserId();
			this.pullUser(po, uid);
			if (StringUtils.isEmpty(id)) {
				po.set("company_id", this.getCompanyId());
				po.save();
				id=po.getStr("id");
			} else {
				po.update();
			}
			Map<String,String> d=new HashMap<String,String>();
			d.put("id",id);
			this.rendJson(true,null, "操作成功！",d);
		} catch (Exception e) {
			log.error("保存产品异常", e);
			this.rendJson(false,null, "保存数据异常！");
		}
	}
	
	public void qryOp() {
		getId();
		Breed m = Breed.dao.findById(id, this.getCompanyId());
		if (m != null)
			this.rendJson(true,null, "", m);
		else
			this.rendJson(false,null, "记录不存在！");
	}
	
	@PowerBind(code={"A2_1_E","A3_1_E"},funcName="删除")
	public void del() {
		try {
			getId();
			Breed.dao.del(id,this.getCompanyId());
			rendJson(true,null,"删除成功！",id);
		} catch (Exception e) {
			log.error("删除异常", e);
			rendJson(false,null,"删除失败！",id);
		}
	}
}
