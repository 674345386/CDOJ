cdoj
.directive("uiTrainingContestResultUploader",
->
  restrict: "E"
  scope:
    result: "="
  controller: [
    "$scope", "$element"
    ($scope, $element) ->
      $scope.hint = "Upload"
      $scope.hasError = false
      dataUploader = new qq.FineUploaderBasic(
        button: $element[0]
        request:
          endpoint: "/training/uploadTrainingContestResult"
          inputName: "uploadFile"
        validation:
          allowedExtensions: ["xls"],
          sizeLimit: 10 * 1024 * 1024 # 10 MB
        multiple: false
        callbacks:
          onComplete: (id, fileName, data) ->
            if data.success == true
              $scope.$apply(->
                $scope.hint = "Successful!"
                $scope.result.fields = data.fields
                $scope.result.users = data.users
                $scope.result.fieldType = data.fieldType
                $scope.hasError = false
              )
            else
              $scope.$apply(->
                $scope.hint = data.error
                $scope.hasError = true
              )
          onProgress: (id, fileName, uploadedBytes, totalBytes) ->
            $scope.$apply(->
              percentage = Math.round(uploadedBytes / totalBytes * 100)
              $scope.hint = "#{percentage} %"
            )
          onError: (id, fileName, errorReason) ->
            $scope.hasError = false
            $scope.hint = errorReason
      )
  ]
  template: """
                <span class="btn btn-sm"
                    ng-class="{'btn-success': hasError == false,
'btn-danger': hasError == true}">
                  <i class="fa fa-upload"></i><span ng-bind="hint"></span>
                </span>
"""
)