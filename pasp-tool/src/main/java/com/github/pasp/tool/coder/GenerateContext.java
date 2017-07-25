package com.github.pasp.tool.coder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.coder.model.EntityInfo;

public class GenerateContext {

	private Config config;

	private List<EntityInfo> entityList;

	private Map<String, EntityInfo> entityMap;

	private List<String> success = new ArrayList<String>();

	private List<String> fail = new ArrayList<String>();

	public void onSuccess(String path) {
		success.add(path);
	}

	public void onFail(String path) {
		fail.add(path);
	}

	public List<String> getSuccess() {
		return success;
	}

	public List<String> getFail() {
		return fail;
	}

	public Config getConfig() {
		return config;
	}

	public List<EntityInfo> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<EntityInfo> entityList) {
		this.entityList = entityList;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Map<String, EntityInfo> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<String, EntityInfo> entityMap) {
		this.entityMap = entityMap;
	}

}
