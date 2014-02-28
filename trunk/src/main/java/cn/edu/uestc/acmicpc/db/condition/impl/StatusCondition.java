package cn.edu.uestc.acmicpc.db.condition.impl;

import cn.edu.uestc.acmicpc.db.condition.base.BaseCondition;
import cn.edu.uestc.acmicpc.db.condition.base.Condition;
import cn.edu.uestc.acmicpc.db.condition.base.Condition.ConditionType;
import cn.edu.uestc.acmicpc.db.condition.base.Condition.JoinedType;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.settings.Global;
import cn.edu.uestc.acmicpc.util.settings.Global.OnlineJudgeResultType;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Status database condition entity.
 */
public class StatusCondition extends BaseCondition {

  public StatusCondition() {
    super("statusId");
  }

  /**
   * Minimal status id.
   */
  @Exp(mapField = "statusId", type = Condition.ConditionType.GREATER_OR_EQUALS)
  public Integer startId;

  /**
   * Maximal status id.
   */
  @Exp(mapField = "statusId", type = Condition.ConditionType.LESS_OR_EQUALS)
  public Integer endId;

  /**
   * Minimal submit time.
   */
  @Exp(mapField = "time", type = Condition.ConditionType.GREATER_OR_EQUALS)
  public Timestamp startTime;

  /**
   * Maximal submit time.
   */
  @Exp(mapField = "time", type = Condition.ConditionType.LESS_OR_EQUALS)
  public Timestamp endTime;

  /**
   * Submit user name.
   */
  public String userName;

  /**
   * Submit user id.
   */
  @Exp(mapField = "userByUserId", type = Condition.ConditionType.EQUALS)
  public Integer userId;

  /**
   * Problem id.
   */
  @Exp(mapField = "problemByProblemId", type = Condition.ConditionType.EQUALS)
  public Integer problemId;

  /**
   * Language.
   */
  @Exp(mapField = "languageByLanguageId", type = Condition.ConditionType.EQUALS)
  public Integer languageId;

  /**
   * Contest id.
   */
  public Integer contestId;

  /**
   * Results.
   *
   * @see OnlineJudgeResultType
   */
  public Set<OnlineJudgeResultType> results = new HashSet<>();

  /**
   * Single result.
   *
   * @see cn.edu.uestc.acmicpc.util.settings.Global.OnlineJudgeResultType
   */
  public OnlineJudgeResultType result;

  @Exp(mapField = "problemByProblemId.isVisible", type = ConditionType.EQUALS)
  public Boolean isVisible;

  @Override
  public Condition getCondition() throws AppException {
    Condition condition = super.getCondition();
    if (contestId != null) {
      if (contestId == -1) {
        condition.addEntry("contestByContestId", Condition.ConditionType.IS_NULL,
            null);
      } else {
        condition.addEntry("contestByContestId", Condition.ConditionType.EQUALS,
            contestId);
      }
    }

    if (userName != null && !userName.equals("")) {
      condition.addEntry("userByUserId.userName", ConditionType.LIKE,
          userName);
    }

    if (result != null) {
      results.add(result);
    }
    if (!results.contains(Global.OnlineJudgeResultType.OJ_ALL)) {
      Condition typeCondition = new Condition(JoinedType.OR);
      Set<Integer> affectedResults = new HashSet<>();
      for (OnlineJudgeResultType result : results) {
        affectedResults.addAll(result.getResults());
      }
      for (Integer result : affectedResults) {
        typeCondition.addEntry("result", ConditionType.EQUALS, result);
      }
      condition.addEntry(typeCondition);
    }
    return condition;
  }

}
