package egovframework.com.cmm;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import egovframework.com.jwt.EgovJwtTokenUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <pre>
 * 공통 DAO
 * - MyBatis 기반 DB 액세스 공통 모듈
 * - Service/Impl 계층에서 직접 주입받아 사용
 * - SQL 구문은 매퍼 XML에 정의 (statementId 기준으로 호출)
 * - select/insert/update/delete 공통 제공
 *
 * [예시]
 *   commonDao.selectOne("AuthMapper.selectUserByUsername", "poisongo");
 *   commonDao.selectList("UserMapper.selectUserList", searchVO);
 * </pre>
 * @author AA
 */
@Component
public class CommonDao {

    /**
     * MyBatis SqlSessionTemplate (Spring에 의해 자동 주입)
     * - 트랜잭션, 커넥션 관리 자동화
     * - SqlSessionFactoryBean으로부터 생성됨
     */
    @Autowired
    private SqlSessionTemplate sqlSession;

    /**ß
     * 단건 조회
     * @param statement  매퍼 XML의 네임스페이스+ID ("AuthMapper.selectUserByUsername" 등)
     * @param parameter  쿼리에 전달할 파라미터(VO, Map, String, int 등)
     * @return           조회 결과(없으면 null)
     * <pre>
     * ex) AuthVO user = commonDao.selectOne("AuthMapper.selectUserByUsername", "poisongo");
     * </pre>
     */
	public <T> T selectOne(String statement, Object parameter) {
        return sqlSession.selectOne(statement, parameter);
    }

    /**
     * 다건(리스트) 조회
     * @param statement  매퍼 XML의 네임스페이스+ID ("UserMapper.selectUserList" 등)
     * @param parameter  쿼리 파라미터(검색VO, Map 등)
     * @return           결과 리스트(List<E>)
     * <pre>
     * ex) List<UserVO> users = commonDao.selectList("UserMapper.selectUserList", searchVO);
     * </pre>
     */
    public <E> List<E> selectList(String statement, Object parameter) {
        return sqlSession.selectList(statement, parameter);
    }

    /**
     * 단건 Insert
     * @param statement  매퍼 XML의 네임스페이스+ID
     * @param parameter  입력 파라미터(VO, Map 등)
     * @return           insert 된 row 수
     * <pre>
     * ex) int cnt = commonDao.insert("UserMapper.insertUser", userVO);
     * </pre>
     */
    public int insert(String statement, Object parameter) {
        return sqlSession.insert(statement, parameter);
    }

    /**
     * 단건 Update
     * @param statement  매퍼 XML의 네임스페이스+ID
     * @param parameter  수정 파라미터(VO, Map 등)
     * @return           update 된 row 수
     * <pre>
     * ex) int cnt = commonDao.update("UserMapper.updateUser", userVO);
     * </pre>
     */
    public int update(String statement, Object parameter) {
        return sqlSession.update(statement, parameter);
    }

    /**
     * 단건 Delete
     * @param statement  매퍼 XML의 네임스페이스+ID
     * @param parameter  삭제 파라미터(VO, Map 등)
     * @return           delete 된 row 수
     * <pre>
     * ex) int cnt = commonDao.delete("UserMapper.deleteUser", userId);
     * </pre>
     */
    public int delete(String statement, Object parameter) {
        return sqlSession.delete(statement, parameter);
    }
}
