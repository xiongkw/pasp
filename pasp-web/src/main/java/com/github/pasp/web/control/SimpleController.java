package com.github.pasp.web.control;

import java.io.Serializable;

import com.github.pasp.core.DTO;
import com.github.pasp.core.Entity;
import com.github.pasp.core.IBaseService;
import com.github.pasp.core.Page;
import com.github.pasp.web.Response;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SimpleController<E extends Entity<ID>, ID extends Serializable, D extends DTO> {
	@Autowired
	private IBaseService<E, ID, D> baseService;

	public Response query(D dto, int pageIndex, int pageSize) {
		Page<D> pageInfo = baseService.queryPage(dto, pageIndex, pageSize);
		return new Response(pageInfo);
	}

	public Response get(ID id) {
		D result = baseService.selectByPrimaryKey(id);
		return new Response(result);
	}

	public Response save(D d) {
		ID id = baseService.insert(d);
		return new Response(id);
	}

	public Response update(D dto) {
		int rows = baseService.updateSelectiveByPrimaryKey(dto);
		return new Response(rows);
	}

	public Response delete(ID id) {
		int rows = baseService.deleteByPrimaryKey(id);
		return new Response(rows);
	}

	protected IBaseService<E, ID, D> getService() {
		return baseService;
	}

	public Response exists(D dto) {
		return new Response(baseService.exists(dto));
	}
}
