/*
 *
 *  * cdoj, UESTC ACMICPC Online Judge
 *  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
 *  * 	mzry1992 <@link muziriyun@gmail.com>
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package cn.edu.uestc.acmicpc.oj.action.admin;

import cn.edu.uestc.acmicpc.checker.ZipDataChecker;
import cn.edu.uestc.acmicpc.db.dao.iface.IProblemDAO;
import cn.edu.uestc.acmicpc.db.dto.impl.ProblemDataDTO;
import cn.edu.uestc.acmicpc.db.entity.Problem;
import cn.edu.uestc.acmicpc.db.view.impl.ProblemDataView;
import cn.edu.uestc.acmicpc.ioc.dao.ProblemDAOAware;
import cn.edu.uestc.acmicpc.ioc.dto.ProblemDataDTOAware;
import cn.edu.uestc.acmicpc.oj.action.file.FileUploadAction;
import cn.edu.uestc.acmicpc.util.FileUtil;
import cn.edu.uestc.acmicpc.util.Global;
import cn.edu.uestc.acmicpc.util.ZipUtil;
import cn.edu.uestc.acmicpc.util.annotation.LoginPermit;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

/**
 * Action for manage problem's data and information.
 * 
 * @author <a href="mailto:muziriyun@gmail.com">mzry1992</a>
 */
@Controller
@LoginPermit(Global.AuthenticationType.ADMIN)
public class AdminProblemDataAction extends FileUploadAction implements ProblemDAOAware,
    ProblemDataDTOAware {

  /**
	 * 
	 */
  private static final long serialVersionUID = -3523496206247311936L;
  @Autowired
  private IProblemDAO problemDAO;
  private Integer targetProblemId;

  @Autowired
  private ProblemDataDTO problemDataDTO;

  private ProblemDataView problemDataView;

  public ProblemDataView getProblemDataView() {
    return problemDataView;
  }

  public void setProblemDataView(ProblemDataView problemDataView) {
    this.problemDataView = problemDataView;
  }

  @Override
  public ProblemDataDTO getProblemDataDTO() {
    return problemDataDTO;
  }

  @Override
  public void setProblemDataDTO(ProblemDataDTO problemDataDTO) {
    this.problemDataDTO = problemDataDTO;
  }

  public Integer getTargetProblemId() {
    return targetProblemId;
  }

  public void setTargetProblemId(Integer targetProblemId) {
    this.targetProblemId = targetProblemId;
  }

  private String getDataZipFileName() {
    return settings.SETTING_UPLOAD_FOLDER + "/problem_" + targetProblemId + ".zip";
  }

  /**
   * Go to problem data editor view!
   * 
   * @return <strong>SUCCESS</strong> signal
   */
  @SkipValidation
  public String toProblemDataEditor() {
    try {
      problemDataView = new ProblemDataView(problemDAO.get(targetProblemId));
    } catch (AppException e) {
      return setError("Specific problem doesn't exist.");
    }
    return SUCCESS;
  }

  /**
   * Upload data file, and modified the data files.
   * <p/>
   * <strong>WARN</strong>: all the origin data files may be deleted.
   * <p/>
   * <strong>INFO</strong>:
   * <p/>
   * All files in data folder should named from 1 to number of cases, input files end with ".in" and
   * output files end with ".out".
   * 
   * @return <strong>JSON</strong> signal.
   */
  @SkipValidation
  public String uploadProblemDataFile() {
    try {
      setSavePath(settings.SETTING_UPLOAD_FOLDER);
      String[] files = uploadFile();
      // In this case, uploaded file should only contains one element.
      if (files == null || files.length != 1)
        throw new AppException("Fetch uploaded file error.");
      File tempFile = new File(files[0]);
      File targetFile = new File(getDataZipFileName());
      if (targetFile.exists() && !targetFile.delete())
        throw new AppException("Internal exception: target file exists and can not be deleted.");
      if (!tempFile.renameTo(targetFile))
        throw new AppException("Internal exception: can not move file.");

      ZipFile zipFile = new ZipFile(getDataZipFileName());
      String tempDirectory = settings.SETTING_UPLOAD_FOLDER + "/" + targetProblemId;
      ZipUtil.unzipFile(zipFile, tempDirectory, new ZipDataChecker());

      File dataPath = new File(tempDirectory);
      File[] dataFiles = dataPath.listFiles();
      assert dataFiles != null;
      json.put("total", dataFiles.length / 2);
      json.put("success", "true");
    } catch (AppException e) {
      json.put("error", e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      json.put("error", "Unknown exception occurred.");
    }
    return JSON;
  }

  /**
   * To update problem test information and compile spj checker in the data folder.
   * <p/>
   * <strong>JSON output</strong>:
   * <ul>
   * <li>
   * For success: {"result":"ok"}</li>
   * <li>
   * For error: {"result":"error", "error_msg":<strong>error message</strong>}</li>
   * </ul>
   * 
   * @return <strong>JSON</strong> signal
   *         <p/>
   */
  @Validations(intRangeFields = {
      @IntRangeFieldValidator(fieldName = "problemDataDTO.timeLimit", min = "0", max = "60000",
          key = "error.timeLimit.validation"),
      @IntRangeFieldValidator(fieldName = "problemDataDTO.javaTimeLimit", min = "0", max = "60000",
          key = "error.timeLimit.validation"),
      @IntRangeFieldValidator(fieldName = "problemDataDTO.memoryLimit", min = "0", max = "262144",
          key = "error.memoryLimit.validation"),
      @IntRangeFieldValidator(fieldName = "problemDataDTO.javaMemoryLimit", min = "0",
          max = "262144", key = "error.memoryLimit.validation"),
      @IntRangeFieldValidator(fieldName = "problemDataDTO.outputLimit", min = "0", max = "262144",
          key = "error.outputLimit.validation") })
  public String updateProblemData() {
    try {
      Problem problem = problemDAO.get(problemDataDTO.getProblemId());
      if (problem == null)
        throw new AppException("No such problem!");

      problemDataDTO.updateEntity(problem);
      String dataPath = settings.JUDGE_DATA_PATH + "/" + targetProblemId;
      String tempDirectory = settings.SETTING_UPLOAD_FOLDER + "/" + targetProblemId;
      File targetFile = new File(dataPath);
      File currentFile = new File(tempDirectory);
      // If the uploaded file list is empty, that means we don't update
      // the data folder.
      int dataCount = 0;
      boolean foundSpj = false;
      File[] files = currentFile.listFiles();
      Map<String, Integer> fileMap = new HashMap<>();
      if (files != null) {
        for (File file : files) {
          if (file.getName().endsWith(".in")) {
            fileMap.put(FileUtil.getFileName(file), ++dataCount);
          } else if (file.getName().equals("spj.cc")) {
            foundSpj = true;
          }
        }
      }
      if (dataCount != 0) {
        FileUtil.clearDirectory(dataPath);
        FileUtil.moveDirectory(currentFile, targetFile);
        for (String file : fileMap.keySet()) {
          File fromFile = new File(dataPath + '/' + file + ".in");
          File toFile = new File(dataPath + '/' + fileMap.get(file) + ".in");
          if (!fromFile.renameTo(toFile))
            throw new AppException("Cannot rename file: " + file + ".in");
          fromFile = new File(dataPath + '/' + file + ".out");
          toFile = new File(dataPath + '/' + fileMap.get(file) + ".out");
          if (!fromFile.renameTo(toFile))
            throw new AppException("Cannot rename file: " + file + ".out");
        }
        problem.setDataCount(dataCount);
        FileUtil.clearDirectory(tempDirectory);
      }

      if (foundSpj) {
        Runtime runtime = Runtime.getRuntime();
        Process process =
            runtime.exec(String.format("g++ %s/spj.cc -o %s/spj -O2", dataPath, dataPath));
        process.waitFor();
        File source = new File(String.format("%s/spj.cc", dataPath));
        if (!source.delete())
          throw new AppException("Cannot remove spj source file");
      }

      problemDAO.update(problem);
      json.put("result", "ok");
    } catch (AppException e) {
      e.printStackTrace();
      json.put("result", "error");
      json.put("error_msg", e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      json.put("result", "error");
      json.put("error_msg", "Unknown exception occurred.");
    }
    return JSON;
  }

  @Override
  public void setProblemDAO(IProblemDAO problemDAO) {
    this.problemDAO = problemDAO;
  }
}