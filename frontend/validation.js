const form=document.getElementById('registrationForm');
const get=id=>document.getElementById(id); const setError=(id,msg)=>{get(id).textContent=msg};
const password=get('password');
password.addEventListener('input',()=>{const v=password.value;let score=0;score+=v.length>=8;score+=/[A-Z]/.test(v);score+=/[a-z]/.test(v);score+=/[0-9]/.test(v);score+=/[^A-Za-z0-9]/.test(v);get('strengthBar').style.width=(score*20)+'%';get('strengthBar').style.background=score<3?'#e4a33a':'#18a77a'});
form.addEventListener('submit',e=>{let ok=true;['nameError','rollError','emailError','courseError','passwordError','fileError','agreeError'].forEach(id=>setError(id,''));
if(get('name').value.trim().length<2){setError('nameError','Enter a valid full name.');ok=false}
if(!/^[A-Za-z0-9/-]{3,40}$/.test(get('roll_number').value.trim())){setError('rollError','Use 3–40 letters, numbers, / or -.');ok=false}
if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(get('email').value.trim())){setError('emailError','Enter a valid email address.');ok=false}
if(!get('course').value){setError('courseError','Choose a course.');ok=false}
if(!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/.test(password.value)){setError('passwordError','Password must include upper/lowercase, number and symbol.');ok=false}
const file=get('profile_file').files[0];if(file&&(!['image/jpeg','image/png','application/pdf'].includes(file.type)||file.size>5*1024*1024)){setError('fileError','Choose JPG, PNG or PDF up to 5 MB.');ok=false}
if(!get('agree').checked){setError('agreeError','Please confirm the information.');ok=false}if(!ok)e.preventDefault();});
