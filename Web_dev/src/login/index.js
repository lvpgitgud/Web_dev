const username = document.getElementById("username");
const email = document.getElementById("email");
const password1 = document.getElementById("password1");
const password2 = document.getElementById("password2");
const emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;

function isValid() {
    let valid = true;

    // Username check
    if (username.value.length < 8) {
        alert('Username must have at least 8 characters');
        valid = false;
    }

    // Email pattern check
    if (!email.value.match(emailPattern)) {
        alert('Invalid email address');
        valid = false;
    }

    // Additional email structure check
    const atpos = email.value.indexOf("@");
    const dotpos = email.value.lastIndexOf(".");
    if (atpos < 1 || (dotpos - atpos < 2)) {
        alert("Please enter a correct email address");
        valid = false;
    }

    // Password match check
    if (password1.value !== password2.value) {
        alert('Passwords do not match');
        valid = false;
    }

    if (valid) {
        alert('Register successful!');
    }

    return valid;
}