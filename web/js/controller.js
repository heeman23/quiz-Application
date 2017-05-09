
var app = angular.module('myApp', ['ngRoute']);
var sessionId;
app.config(function ($routeProvider) {
    $routeProvider
              .when("/index", {
                templateUrl: "index.html",
                controller: "indexcontroller"
            })
            .when("/signup", {
                templateUrl: "signup.html",
                controller: "signupcontroller"
            })
            .when("/login", {
                templateUrl: "login.html",
                controller: "logincontroller"
            })
            .when("/forget", {
                templateUrl: "forgetpassword.html",
                controller: "forgetcontroller"
            })
            .when("/quiz", {
                templateUrl: "quiz.html",
                controller: "quizcontroller"
            })
            .otherwise({
                redirectTo: "index.html"
            });


});

app.controller('logincontroller', function ($scope, $http, $location) {
    $scope.loginVarify = function () {
        alert($scope.email);
        alert($scope.password);
        var email = $scope.email;
        var password = $scope.password;
        var params = {
            email: email,
            password: password,
            flag: 'L'
        };
        $http({
            method: "POST",
            url: "login",
            //data: "email=" + email + "&password=" + password,
            params: params,
        }).then(function mySuccess(response) {
            sessionId = response.data[0];
            var flag = response.data[1];
            // localStorage.setItesecuritpasswordyDatam("sessionId", sessionId);

            if (flag === 'L' && sessionId !== '') {
                alert(sessionId);
                $scope.myWelcome = "welcome " + email;

                $location.url("/quiz");

            } else if (flag === '' && sessionId === '') {
                $scope.myWelcome = "session out"
                $location.url("/login.html");
            } else {

                $scope.myWelcome = "user does not exist";
            }
        }, function myError(response) {
            alert(response.data)

            $scope.myWelcome = response.statusText;
        });
    }
});
app.controller('signupcontroller', function ($scope, $http, $location) {
    $scope.signup = function () {

        var username = $scope.username;
        var email = $scope.email;
        var password = $scope.password;
        var confirmpassword = $scope.confirmpassword;
        var DOB = $scope.DOB;
        var contact = $scope.contact;
        var gender = $scope.gender;
        var securityQuestion = $scope.securityQuestion;
        var securityAnswer = $scope.securityAnswer;
        if (password !== confirmpassword) {
            $scope.err = 'password doesnot match with confirm password';
        } else {
            var params = {
                username: username,
                email: email,
                password: password,
                DOB: DOB,
                contact: contact,
                gender: gender,
                securityQuestion: securityQuestion,
                securityAnswer: securityAnswer,
                flag: 'S'
            };
            $http({
                method: "POST",
                url: "login",
                params: params,
            }).then(function mySuccess(response) {
                var dataSaved = response.data;
//                alert(dataSaved);
                $location.url("/login");

            }, function myError(response) {
                alert(response.data)

                $scope.myWelcome = response.statusText;
            });
        }
    };
});
app.controller('forgetcontroller', function ($scope, $http, $location) {
//    alert("inside forget controller");
    var email;
    $scope.getQuestion = true;
    $scope.verifyAnswer = false;
    $scope.resetPassword = false;
    var securityAnswer;
    $scope.securityQuestion = function () {
//        alert("inside security question method")
//        alert($scope.email);
        email = $scope.email;
        var params = {
            email: email,
            flag: 'F'
        };
        $http({
            method: "POST",
            url: "forget",
            params: params,
        }).then(function mySuccess(response) {
            var securityData = response.data;
//            alert(securityData)
            var securityQuestion = securityData[0];
            securityAnswer = securityData[1];
            alert(securityQuestion);
            if (securityQuestion !== '') {
                $scope.getQuestion = false;
                $scope.verifyAnswer = true;
                $scope.securityq = securityQuestion;
            } else {
                $scope.myMail = "mail doesnot exist";
            }
        }, function myError(response) {
            alert(response.data);
        });
    }
    $scope.checkAnswer = function () {
//        alert("inside security question method")
        var answer = $scope.securityA;

        if (securityAnswer.toString().toLowerCase() === answer.toString().toLowerCase()) {
            $scope.getQuestion = false;
            $scope.verifyAnswer = false;
            $scope.resetPassword = true;
        } else {
            $scope.answerErr = "Enter correct answer";
        }
    }
    $scope.submitPassword = function () {
        var password = $scope.password;
        var confirmpassword = $scope.confirmpassword;
        if (password !== confirmpassword) {
            $scope.err = 'password doesnot match with confirm password';
        } else {
            var params = {
                password: password,
                email: email,
                flag: 'R'
            };
            $http({
                method: "POST",
                url: "forget",
                params: params,
            }).then(function mySuccess(response) {
//                alert(response.data);
                $scope.err = "";
                $scope.securityq = "";
                $scope.email = "";
                $scope.securityA = "";
                $scope.successText = "Congratulations! your password reset successfully";
                $location.url("/login.html");
            }, function myError(response) {
                alert(response.data);
                $scope.successText = "Try Again!";
            });
        }
    }
});

app.controller("quizcontroller", function ($scope, $http, $location) {


    $scope.selectCategory = true;
    $scope.quizForm = false;
    $scope.getQuestions = function () {
        var category = $scope.categories;
        var params = {
            category: category,
            flag: 'G',
        };
        $http({
            method: "POST",
            url: "getQuestion",
            params: params,
        }).then(function mySuccess(response) {
            $scope.messages = response.data;
            alert($scope.messages);
            $scope.quizForm = true;
            $scope.selectCategory = false;

            $scope.answerList = [];
            angular.forEach($scope.messages, function (key, value) {
                $scope.answerList.push(key.answer);

            });



        }, function myError(response) {
            alert(response.data);
            $scope.successText = "Try Again!";
        });
    }

    $scope.quizSubmit = function () {
        $scope.score = 0;
        $scope.correctanswer = $scope.answerList.reverse();
        for (var i = 1; i <= 10; i++) {
            var Correctanswer = $scope.correctanswer.pop();
            $("table[id=" + i + "]").find("td input[type=radio]").each(function () {
                if ($(this).attr("checked") === 'checked') {
                    if ($(this).val() === Correctanswer) {
                        $scope.score++;
                        $(this).parent("td").addClass("rightanswer");
                    } else {
                        $scope.score--;
                        $(this).parent("td").addClass("wronganswer");
                    }

                } else {

                }


            });


        }
        alert("final score" + $scope.score);


    }
//     } else {
//        $location.url("/login");
//        alert("session time out")
//    }
});
app.controller("indexcontroller", function ($scope, $http, $location) {
alert("ka haal hain");
});

 
