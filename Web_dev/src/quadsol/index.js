 function solve() {
      const a = parseFloat(document.getElementById('a').value);
      const b = parseFloat(document.getElementById('b').value);
      const c = parseFloat(document.getElementById('c').value);
      const result = document.getElementById('result');

      if (isNaN(a) || isNaN(b) || isNaN(c)) {
        alert( "Please enter all coefficients (a, b, c).");
        return;
      }

      if (a === 0) {
        alert("This is not a quadratic equation (a ≠ 0).");
        return;
      }

      const discriminant = b * b - 4 * a * c;

      if (discriminant > 0) {
        const root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        const root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
        result.innerHTML = `Two real roots:<br>x₁ = ${root1.toFixed(3)}<br>x₂ = ${root2.toFixed(3)}`;
      } 
      else if (discriminant === 0) {
        const root = -b / (2 * a);
        result.innerHTML = `One real root:<br>x = ${root.toFixed(3)}`;
      } 
      else {
        const real = (-b / (2 * a)).toFixed(3);
        const imag = (Math.sqrt(-discriminant) / (2 * a)).toFixed(3);
        result.innerHTML = `Complex roots:<br>x₁ = ${real} + ${imag}i<br>x₂ = ${real} - ${imag}i`;
      }
    }