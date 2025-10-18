const username = document.getElementById("username");
const email = document.getElementById("email");
const dob = document.getElementById("dob");
const password2 = document.getElementById("password2");
const emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;

function isValid() {
    let valid = true;


    if (username.value.length < 8) {
        alert('Username must have at least 8 characters');
        valid = false;
    }

    if (!email.value.match(emailPattern)) {
        alert('Invalid email address');
        valid = false;
    }


    const atpos = email.value.indexOf("@");
    const dotpos = email.value.lastIndexOf(".");
    if (atpos < 1 || (dotpos - atpos < 2)) {
        alert("Please enter a correct email address");
        valid = false;
    }

 
    if (dob.value ='') {
        alert('Date of birth empty');
        valid = false;
    }

    if (valid) {
        alert('Register successful!');
    }

    return valid;
}