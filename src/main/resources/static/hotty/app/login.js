angular.module('signIn', [])
    .controller('SignInController', ['$scope', '$http', '$window', function ($scope, $http, $window) {
        $scope.user = {};

        $scope.signIn = function () {
            var postUrl = '/hotty/authenticate';

            var encodedData =
                "username=" + encodeURIComponent($scope.user.username) +
                "&password=" + encodeURIComponent($scope.user.password);

            console.info("PostUrl: " + postUrl + " encodedData: " + encodedData);

            $http({
                method: 'POST',
                url: postUrl,
                data: encodedData,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}

            }).then(function success(response) {
                console.info("Response: " + JSON.stringify(response) + " for URL: " + postUrl);
                if (response.status == 401 || response.error) {
                    $scope.error = response.error;

                } else {
                    $scope.message = response.message;
                }
                $window.location.href = "/hotty/app/index.html";

            }, function fail(response) {
                console.error("Can't login url: " + postUrl +
                    " user: " + JSON.stringify($scope.user) +
                    " response: " + JSON.stringify(response));
            });
        };
    }]);
