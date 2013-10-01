package cn.edu.uestc.acmicpc.oj.service.iface;

import java.util.List;

import cn.edu.uestc.acmicpc.db.condition.base.Condition;
import cn.edu.uestc.acmicpc.db.condition.impl.ProblemCondition;
import cn.edu.uestc.acmicpc.db.dao.impl.ProblemDAO;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemDTO;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemListDTO;
import cn.edu.uestc.acmicpc.db.entity.Problem;
import cn.edu.uestc.acmicpc.oj.view.PageInfo;
import cn.edu.uestc.acmicpc.util.exception.AppException;

/**
 * Service interface for {@link Problem}.
 */
public interface ProblemService extends OnlineJudgeService<Problem, Integer> {

  /**
   * Get all visible problems' id without any statements.
   *
   * @return all problem id list
   * @throws AppException
   */
  List<Integer> getAllVisibleProblemIds() throws AppException;
  
  /**
   * Get specific problem by problem's ID.
   * 
   * @param Integer
   * @return ProblemDTO
   * @throw AppException
   */
  ProblemDTO getProblemDTOByProblemId(Integer problemId) throws AppException;
  
  /**
   * Translate problem to problemDTO. 
   * 
   * @param Problem
   * @return ProblemDTO
   * @throws AppException
   */
  ProblemDTO getProblemDTOByProblem(Problem problem) throws AppException;
  
  /**
   * Translate problem to problemListDTO.
   * 
   * @param problem
   * @return ProblemListDTO
   * @throws AppException
   */
  ProblemListDTO getProblemListDTOByProblem(Problem problem) throws AppException;
  
  /**
   * Get number of problems that meet the condition.
   * 
   * @param condition
   * @return Long
   * @throws AppException
   */
  Long count(Condition condition) throws AppException;
  
  /**
   * Get problems list that meet the condition and inside the range of page
   * @param problemCondition
   * @param pageInfo
   * @return ProblemDTO List
   * @throws AppException
   */
  List<ProblemListDTO> GetProblemListDTOList(ProblemCondition problemCondition, 
      PageInfo pageInfo) throws AppException;
}