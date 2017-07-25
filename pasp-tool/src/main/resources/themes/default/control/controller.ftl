package ${basePackage}.control.${moduleName};

import ${basePackage}.service.${moduleName}.dto.${entityName}DTO;
import ${basePackage}.data.${moduleName}.entity.${entityName};
<#if !pk.fullJavaType?starts_with("java.lang")>
import {pk.fullJavaType};
</#if>
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pasp.web.Response;
import com.github.pasp.web.control.SimpleController;

@RestController
@RequestMapping("/${moduleName}")
public class ${entityName}Controller extends SimpleController<${entityName}, ${pk.javaType}, ${entityName}DTO> {
	private Logger logger = LoggerFactory.getLogger(${entityName}Controller.class);
	
	@Override
	@RequestMapping(value = "${entityName?lower_case}s", method = RequestMethod.GET)
	public Response query(${entityName}DTO dto,
			@RequestParam(required = false, defaultValue = "1") int pageIndex,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {
		logger.debug("Request for ${entityName?lower_case}s");
		return super.query(dto, pageIndex, pageSize);
	}

	@Override
	@RequestMapping(value = "${entityName?lower_case}s/{id}", method = RequestMethod.GET)
	public Response get(@PathVariable ${pk.javaType} id) {
		return super.get(id);
	}

	@Override
	@RequestMapping(value = "${entityName?lower_case}s", method = RequestMethod.POST)
	public Response save(@RequestBody ${entityName}DTO d) {
		return super.save(d);
	}

	@Override
	@RequestMapping(value = "${entityName?lower_case}s", method = RequestMethod.PUT)
	public Response update(@RequestBody ${entityName}DTO dto) {
		return super.update(dto);
	}

	@Override
	@RequestMapping(value = "${entityName?lower_case}s/{id}", method = RequestMethod.DELETE)
	public Response delete(@PathVariable ${pk.javaType} id) {
		return super.delete(id);
	}
	
	@Override
	@RequestMapping(value = "${entityName?lower_case}s/exists", method = RequestMethod.GET)
	public Response exists(${entityName}DTO dto) {
		return super.exists(dto);
	}

}
