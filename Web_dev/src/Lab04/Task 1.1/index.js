        const username = document.getElementById("username");
        const email = document.getElementById("email");
        const password = document.getElementById("password");
        const passwordConfirm = document.getElementById("passwordConfirm");
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const usernameRegex = /^[a-zA-Z0-9]+$/;
        const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
        const submitBtn = document.getElementById("submitBtn");
        let message;

        function validateUsername() {
            
            if(username.value.length < 4 ||username.value.length >20 || !(usernameRegex.test(username.value))){
                showError("username","Username must be between 4 and 20 characters long and alphanumerical");
                return false;
            }else{
                clearError("username");
                return true;
            }

        }
        
        function validateEmail() {
            if(!email.value.match(emailRegex)){
                showError("email","Invalid email address");
                return false;
            }else{
                clearError("email");
                return true;
            }

        }
        
        function validatePassword() {
            // TODO: Check length >= 8, has uppercase, has number
            if(!(password.value.length >=8 && (passwordRegex.test(password.value)))){
                showError("password","Password must be longer than 8 and contain at least one uppercase letter and one lowercase letter");
                return false;
            }else{
                clearError("password");
                return true;
            }
        }
        
        function validatePasswordMatch() {
            if(password.value !== passwordConfirm.value){
                showError("passwordConfirm","Repeated password does not match");
                return false;
            } else{
                clearError("passwordConfirm");
                return true;
            }
            
        }
        
        function showError(fieldId, message) {
            let error = document.getElementById(fieldId+'Error');
            error.innerHTML = "<p>"+message+"</p>";
        }
        
        function clearError(fieldId) {
            let error = document.getElementById(fieldId+'Error');
            error.innerHTML = " ";
        }
        
        function validateForm() {
                const validUsername = validateUsername();
                const validEmail = validateEmail();
                const validPassword = validatePassword();
                const validMatch = validatePasswordMatch();
                const allValid = validUsername && validEmail && validPassword && validMatch;
                submitBtn.disabled = !allValid;
                return allValid;
        }
        
        username.addEventListener("input", validateForm);
        email.addEventListener("input", validateForm);
        password.addEventListener("input", validateForm);
        passwordConfirm.addEventListener("input", validateForm);
        
        
        document.getElementById('signupForm').addEventListener('submit', function(e) {
            e.preventDefault();
            if (validateForm()) {
                alert("Form submitted successfully!");
                signupForm.reset();
                submitBtn.disabled = true;
            }
        });