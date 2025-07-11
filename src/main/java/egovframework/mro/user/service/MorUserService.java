package egovframework.mro.user.service;

import java.util.List;

import egovframework.com.cmm.CommonDao;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.mro.uat.service.MroLoginService;
import egovframework.mro.user.service.model.UserInfVO;
import lombok.RequiredArgsConstructor;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 게시판 이용정보를 관리하기 위한 서비스 구현 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성 
 *
 * </pre>
 */
@Service("MorUserService")
@RequiredArgsConstructor
public class MorUserService extends EgovAbstractServiceImpl {
	
    private final UserInfoDAO userInfoDAO;

    private final CommonDao cmmonDao;
    

   	public List<EgovMap> selectUserList(UserInfVO userInfVO) throws Exception {
   		return cmmonDao.selectList("UserRequestDTO.selectUserList", userInfVO);
//		return userInfoDAO.selectUserList(userInfVO);
    }

    
 
}