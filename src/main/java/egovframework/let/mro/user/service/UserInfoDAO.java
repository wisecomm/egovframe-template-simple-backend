package egovframework.let.mro.user.service;

import java.util.List;
import java.util.Map;

import egovframework.let.cop.com.service.BoardUseInf;
import egovframework.let.cop.com.service.BoardUseInfVO;
import egovframework.let.mro.user.model.UserInfModel;
import egovframework.let.mro.user.model.UserInfVO;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import org.springframework.stereotype.Repository;

/**
 * 게시판 이용정보를 관리하기 위한 데이터 접근 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일       수정자           수정내용
 *  -------     --------    ---------------------------
 *   2009.04.02  이삼섭          최초 생성
 *   2011.05.31  JJY           경량환경 커스터마이징버전 생성
 *
 * </pre>
 */
@Repository("UserInfoDAO")
public class UserInfoDAO extends EgovAbstractMapper {

	// vo model 뭐가 맞는지 ???
	public List<Object> selectUserList(UserInfVO userInfVO) throws Exception {
		return selectList("UserRequestDTO.selectUserList", userInfVO);
	}
	

}