function validateDates() {
        var intro = $("#introduced").val();
        var disco = $("#discontinued").val();
        var dateMin = new Date(0);
        var dateMax = new Date();
        if (intro == "" && disco == "") {
            return true;
        }
        if (intro != "") {
            try {
                var dateIntro = new Date($("#introduced").val());
                if(dateIntro < dateMin || dateIntro > dateMax) {
                    alert("Wrong intro date !");
                    return false;
                }
            } catch (err) {
                alert("Wrong intro date !");
                return false;
            }
        }

        if (disco != "") {
            try {
                var dateDisco = new Date($("#discontinued").val());
                if(dateDisco < dateMin || dateDisco > dateMax) {
                    alert("Wrong discontinued date !");
                    return false;
                }
            } catch (err) {
            alert("Wrong discontinued date !");
            return false;
            }
        }
        if (disco != "" && intro != "") {
            if (disco < intro) {
                alert("Discontinued before introduced !");
                return false;
            }
        }
        return true;
    }