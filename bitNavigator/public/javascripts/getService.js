/**
 * Created by ognjen.cetkovic on 21/09/15.
 */
var serviceValue = document.getElementById("service");
var services = document.getElementById("service-list");

function changeService() {
    serviceValue.value = services.options[services.selectedIndex].text;
};
services.onchange = changeService;