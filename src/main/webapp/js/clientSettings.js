var userData = null;

async function getUserClientDataServlet(){
    let response = await fetch('/ClientProfile', {method: 'get'})
    let userData = response.json();
    return userData;
}

async function setDataProfile(){
    userData = await getUserClientDataServlet();
    console.log(userData);
    clientName = document.getElementById("clientName");
    clientName.innerHTML = userData.name + " " + userData.lastname + " ";

    setUserDataIntoForm(userData);
}

function setUserDataIntoForm(userData){
    document.getElementById("firstnameForm").value = userData.name;
    document.getElementById("lastnameForm").value = userData.lastname;
    document.getElementById("emailForm").value =userData.email;
}

function getUserDataFromForm(){
    return {
        "firstname": document.getElementById("firstnameForm").value ,
        "lastname": document.getElementById("lastnameForm").value ,
        "email": document.getElementById("emailForm").value,
        "newPasswordRepited" : document.getElementById("newPasswordRepited").value,
        "newPassword" : document.getElementById("newPassword").value,
        "currentPassword" : document.getElementById("currentPassword").value
    }
}


async function saveUserData(){
    console.log("Guardando los datos del usuario");
    userData = getUserDataFromForm();

    let userInfo = { firstname: userData.firstname, lastname: userData.lastname, email: userData.email };

    if(userData.newPassword){

        console.log("El usuario intenta cambiar su contraseña")
        if (userData.newPassword != userData.newPasswordRepited){
            alert("New password don't match");
            return;
        } else{
            userInfo["currentPassword"] = userData.currentPassword;
            userInfo["newPassword"] = userData.newPassword;
        }

    }
    
    response = await postDataToServlet("/ClientProfile", userInfo);
    console.log("La respuesta es ", response);
    alert(response.description);
}


// TODO: Pasar a un nivel superior de funciones comunes
async function postDataToServlet(servlet, json) {

    function encodeFormData(data){
        return Object.keys(data)
            .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(data[key])).join('&');
    }

    console.log("Aqui se inicia la petición al servlet...");

    let response = await  fetch(servlet, {
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}, method: 'POST', body: encodeFormData( json )
    });
    return await response.json();
}

setDataProfile();
