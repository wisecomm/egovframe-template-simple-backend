package egovframework.let.mro.user.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.ResultVoHelper;
import egovframework.com.cmm.web.EgovFileDownloadController;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.cop.bbs.domain.model.BoardMasterVO;
import egovframework.let.cop.bbs.domain.model.BoardVO;
import egovframework.let.cop.bbs.dto.request.BbsSearchRequestDTO;
import egovframework.let.cop.bbs.service.EgovBBSAttributeManageService;
import egovframework.let.cop.bbs.service.EgovBBSManageService;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.cop.com.service.EgovBBSUseInfoManageService;
import egovframework.let.mro.user.model.UserInfModel;
import egovframework.let.mro.user.model.UserInfVO;
import egovframework.let.mro.user.model.UserRequestDTO;
import egovframework.let.mro.user.service.MorUserService;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("/mro")
@RequiredArgsConstructor
@Tag(name="MroUserController",description = "사용자 관리")
public class MroUserController {
	private final MorUserService morUserService;
	private final EgovPropertyService propertyService;
	private final EgovBBSAttributeManageService bbsAttrbService;
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

	    List<Object> listObject = morUserService.selectUserList(userInfVO);

	    resultMap.put("resultList", listObject);
	    resultMap.put("resultCnt", listObject.size());
	    resultMap.put("paginationInfo", paginationInfo);

	    return resultVoHelper.buildFromMap(resultMap, ResponseCode.SUCCESS);
	}

}