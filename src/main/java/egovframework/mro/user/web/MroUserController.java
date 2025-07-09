package egovframework.mro.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;

import egovframework.mro.user.service.MorUserService;
import egovframework.mro.user.service.model.UserInfVO;
import egovframework.mro.user.service.model.UserRequestDTO;

/**
 * 사용 관리를 위한 컨트롤러 클래스
 * @author danyoh
 * @since 2026.06.23
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자             수정내용
 *  -------    --------        ---------------------------
 *  2026.06.23  danyoh            최초 생성
 *
 *  </pre>
 */
@RestController
@RequestMapping("/mrouser")
@RequiredArgsConstructor
@Tag(name="MroUserController",description = "사용자 관리")
public class MroUserController {
	private final MorUserService morUserService;
	private final EgovPropertyService propertyService;
	private final DefaultBeanValidator beanValidator;
	private final ResultVoHelper resultVoHelper;

	/**
	 * 게시판 사용정보 목록을 조회한다.
	 *
	 * @param request
	 * @param bdUseVO
	 * @return
	 * @throws Exception
	 */
	@Operation(
			summary = "사용자 목록 조회",
			description = "사용자 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"MroUserController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value ="selectUserList")
	public ResultVO selectUserList(HttpServletRequest request,
			@ModelAttribute UserRequestDTO userRequestDTO) throws Exception {
		UserInfVO userInfVO = new UserInfVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		userInfVO.setPageIndex(userRequestDTO.getPageIndex());
		userInfVO.setSearchWrd(userRequestDTO.getSearchWrd());
		userInfVO.setPageUnit(propertyService.getInt("Globals.pageUnit"));
		userInfVO.setPageSize(propertyService.getInt("Globals.pageSize"));

		PaginationInfo paginationInfo = new PaginationInfo();

		paginationInfo.setCurrentPageNo(userInfVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(userInfVO.getPageUnit());
	    paginationInfo.setPageSize(userInfVO.getPageSize());

	    userInfVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    userInfVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    userInfVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	    List<EgovMap> listObject = morUserService.selectUserList(userInfVO);

	    resultMap.put("resultList", listObject);
	    resultMap.put("resultCnt", listObject.size());
	    resultMap.put("paginationInfo", paginationInfo);

	    return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

}