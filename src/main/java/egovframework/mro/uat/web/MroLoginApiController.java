package egovframework.mro.uat.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.mro.uat.service.MroLoginService;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name="MroLoginApiController",description = "로그인 관련")
@RequestMapping("/mrologin")  // URL 프리픽스; 최종 경로는 /auth/login
public class MroLoginApiController {

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	/** JWT */
    private final EgovJwtTokenUtil jwtTokenUtil;
	
	private final MroLoginService loginService;
	
	@Operation(
			summary = "JWT 로그인",
			description = "JWT 로그인 처리",
			tags = {"EgovLoginApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "300", description = "로그인 실패")
	})
	@PostMapping(value = "/login-jwt")
	public HashMap<String, Object> actionLoginJWT(@RequestBody LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		// 1. JWT 로그인 처리
		LoginVO loginResultVO = loginService.actionLogin(loginVO);
		
		if (loginResultVO != null && loginResultVO.getId() != null && !loginResultVO.getId().equals("")) {
			if(loginResultVO.getGroupNm().equals("ROLE_ADMIN")) {//로그인 결과에서 스프링시큐리티용 그룹명값에 따른 권한부여
				loginResultVO.setUserSe("ADM");
	        }
			log.debug("===>>> loginResultVO.getUserSe() = "+loginResultVO.getUserSe());
			log.debug("===>>> loginResultVO.getId() = "+loginResultVO.getId());
			log.debug("===>>> loginResultVO.getPassword() = "+loginResultVO.getPassword());
			log.debug("===>>> loginResultVO.getGroupNm() = "+loginResultVO.getGroupNm());//로그인 결과에서 스프링시큐리티용 그룹명값 출력
			
			String jwtToken = jwtTokenUtil.generateToken(loginResultVO);
			
			String username = jwtTokenUtil.getUserSeFromToken(jwtToken);
	    	log.debug("Dec jwtToken username = "+username);
	    	String groupnm = jwtTokenUtil.getInfoFromToken("groupNm", jwtToken);
	    	log.debug("Dec jwtToken groupnm = "+groupnm);//생성한 토큰에서 스프링시큐리티용 그룹명값 출력
	    	//서버사이드 권한 체크 통과를 위해 삽입
	    	//EgovUserDetailsHelper.isAuthenticated() 가 그 역할 수행. DB에 정보가 없으면 403을 돌려 줌. 로그인으로 튕기는 건 프론트 쪽에서 처리
	    	request.getSession().setAttribute("LoginVO", loginResultVO);
	    	
			resultMap.put("resultVO", loginResultVO);
			resultMap.put("jToken", jwtToken);
			resultMap.put("resultCode", "200");
			resultMap.put("resultMessage", "성공 !!!");
			
		} else {
			resultMap.put("resultVO", loginResultVO);
			resultMap.put("resultCode", "300");
			resultMap.put("resultMessage", egovMessageSource.getMessage("fail.common.login"));
		}
		
		return resultMap;
	}
	
}
