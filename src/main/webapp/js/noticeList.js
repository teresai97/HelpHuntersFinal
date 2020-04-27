var dataNoticesGlobal = null;

function noticeList () {

    // Completar obtenciçón de valores
    console.log("Aqui se inicia la petición al servlet...");

    fetch('/NoticeList', {
        method: 'get',
    }).then(function(response){
        response.json().then( (data)=> {

            console.log("Data que regresa mi servlet: ", data);

            dataNoticesGlobal = data;

            var html = "";

            for (var i = 0; i < data.length; i++) {
                record = data[i];

                html += `
                     <!-- Job Listing -->
                    <a onclick="openPopupNotice(${i})" class="job-listing">
                    
                    <!-- Job Listing Details -->
                    <div class="job-listing-details">
                    
                    <!-- Details -->
                    <div class="job-listing-description">
                    <h3 class="job-listing-title">Notice ${record.subject} concerning ${record.caregiverfirstname} ${record.caregiverlastname}</h3>
                    
                    <!-- Job Listing Footer -->
                    <div class="job-listing-footer">
                    <ul>
                    <li><i class="icon-material-outline-access-time"></i> Date created: ${record.datecreated}</li>
                    ${ (record.datereplied) ? '<li><i class="icon-material-outline-access-time"></i> Date replied:' + record.datereplied + '</li>' : '<li><i class="icon-material-outline-access-time"></i> Date replied: Notice has not been replied yet </li>'}
                    </ul>
                    </div>
                    </div>
                    
                    <!-- Bookmark -->
                    <span class="bookmark-icon"></span>
                    </div>
                    </a>
                `
            }

            dinamicContent.innerHTML = html;
            console.log("La respuesta es", data);

       });
    })

}

function openPopupNotice (i) {

    record = dataNoticesGlobal[i];

    if (!record.datereplied) {
        $.magnificPopup.open({
            items: {
                src: `
                <div  class="container-card">
                
                <div class="sign-in-form">
                
                <div class="popup-tabs-container">
                
                <div class="popup-tab-content" id="notice">
                
                <!-- Welcome Text -->
                <div class="welcome-text">
                <h3>Notice Details</h3>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Subject:</h5>
                </div>
                <p>${record.subject}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>This notice concerns caregiver:</h5>
                </div>
                <p>${record.caregiverfirstname} ${record.caregiverlastname}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Notice's Text:</h5>
                </div>
                <p>${record.message}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Date created:</h5>
                </div>
                <p>${record.datecreated}</p>
                </div>
    
                </div>
        
                </div>
                </div>
                </div>`, // can be a HTML string, jQuery object, or CSS selector

                type: 'inline'
            }
        });

    } else {
        $.magnificPopup.open({
            items: {
                src: `
                <div  class="container-card">
                
                <div class="sign-in-form">
                
                <div class="popup-tabs-container">
                
                <div class="popup-tab-content" id="notice">
                
                <!-- Welcome Text -->
                <div class="welcome-text">
                <h3>Notice Details</h3>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Subject:</h5>
                </div>
                <p>${record.subject}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>This notice concerns caregiver:</h5>
                </div>
                <p>${record.caregiverfirstname} ${record.caregiverlastname}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Notice's Text:</h5>
                </div>
                <p>${record.message}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Date created:</h5>
                </div>
                <p>${record.datecreated}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>This notice was replied by:</h5>
                </div>
                <p>${record.administratorfirstname} ${record.administratorlastname}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Reply:</h5>
                </div>
                <p>${record.message}</p>
                </div>
                
                <div class="col-xl-6 col-md-6">
                <div class="section-headline margin-top-25 margin-bottom-12">
                <h5>Date replied:</h5>
                </div>
                <p>${record.datereplied}</p>
                </div>
    
                </div>
        
                </div>
                </div>
                </div>`, // can be a HTML string, jQuery object, or CSS selector

                type: 'inline'
            }
        });
    }
}

noticeList();