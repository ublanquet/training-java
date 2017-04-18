//On load
$(function () {
    // Default: hide edit mode
    $(".editMode").hide();

    // Click on "selectall" box
    $("#selectall").click(function () {
        $('.cb').prop('checked', this.checked);
    });

    // Click on a checkbox
    $(".cb").click(function () {
        if ($(".cb").length == $(".cb:checked").length) {
            $("#selectall").prop("checked", true);
        } else {
            $("#selectall").prop("checked", false);
        }
        if ($(".cb:checked").length != 0) {
            $("#deleteSelected").enable();
        } else {
            $("#deleteSelected").disable();
        }
    });


    var pageN = 0;
    var perPage = 10;
    var table = $("tbody");
    var totalCount = $("#totalCount").val();
    var filteredCount = $("#filteredCount").val();
    var pageMax = 1;
    var orderBys = [];
    var pagesStartIndex = 0;

    //$("ul.pagination li").eq(1).addClass("active");
    $("#p0").addClass("active");
    setPages();

    //Pagination
    $("ul.pagination a").click(function () {
        $("ul.pagination li").removeClass("active");
        $(this).parent().addClass("active");
        pageN = $(this).text();
        var filter = $("#searchbox").val();
        //console.log( this.text() );
        console.log( pageN );

        ajaxTableReload();
    });

    $("#prev").unbind( "click" ).click(function () {
        if (pageN > 0) {
            pageN--;
            var filter = $("#searchbox").val();
            $("ul.pagination li").removeClass("active");
            $("#p"+ (pageN - pagesStartIndex)).addClass("active")
            ajaxTableReload();
        }
    });
    $("#next").unbind( "click" ).click(function () {
        if (pageN < pageMax-1) {
            pageN++;
            var filter = $("#searchbox").val();
            $("ul.pagination li").removeClass("active");
            $("#p" + (pageN - pagesStartIndex)).addClass("active")
            ajaxTableReload();
        }
    });

    $("#prevPages").unbind( "click" ).click(function () {
        if (pagesStartIndex > 0) {
            pagesStartIndex -= 60;
            setPages();
        }
    });
    $("#nextPages").unbind( "click" ).click(function () {
        if (pagesStartIndex < pageMax-60) {
            pagesStartIndex += 60;
            setPages();
        }
    });



    $("button.nbEntries").click(function () {
        $("button.nbEntries").removeClass("active");
        $(this).addClass("active");
        perPage = $(this).text();
        setPages();
    });

    $("#searchsubmit").click(function () {
        ajaxTableReload();
    });

    //$("#searchForm").submit().
    $("#searchForm").submit(function (e) {
        event.preventDefault();
        ajaxTableReload();
    });

    function ajaxTableReload() {
        var filter = $("#searchbox").val();
        $.post(contextPath + "/dashboard/ajax", {
            pageN: pageN,
            perPage: perPage,
            search: filter,
            order: orderBys
        }, function(response) {
            table.html(response);
            $(".editMode").hide();
            filteredCount = $("#filteredCount").val();
            var count = filteredCount || totalCount;
            $("#homeTitle").text( count + " Computers found");
            setPages();
        });
    }

    function setPages () {
            var count = filteredCount || totalCount;
            var maxPages = count / perPage;
            var lastPage = count % perPage == 0 ? 0 : 1;
            pageMax = maxPages+lastPage;
            if(pageMax < 60) {
                $(".pageNum").each(function (i) {
                    if (i > maxPages + lastPage) {
                        $(this).hide();
                    } else {
                        $(this).show();
                    }
                });
            }
            else {
                if(pagesStartIndex == 0) {
                    $(".pageNum").each(function (i) {
                        if (i > maxPages + lastPage) {
                            $(this).hide();
                        } else {
                            $(this).show();
                        }
                    });
                }
                else {
                    $(".pageNum a").each(function (i) {
                        var pN = $(this).text();
                        //$(this).attr("id", "p" + (parseInt(pN)+pagesStartIndex))
                        $(this).text( parseInt(pN)+pagesStartIndex );

                        if (i + pagesStartIndex > maxPages + lastPage) {
                            $(this).hide();
                        } else {
                            $(this).show();
                        }

                    });
                }

            }
        //$("ul.pagination li").last().show();
        $("#next").show();
        $("#nextPages").show();
    }

     $(".ordering").click( function() {
         var $this = $(this);
         var elem = $(this);
         var id = $this.attr("id");
         var column = $(elem).attr("id");
         //var column = $(elem).getAttribute("id");
         if ($(elem).hasClass("glyphicon-sort")) {
             $(elem).removeClass("glyphicon-sort");
             $(elem).addClass("glyphicon-sort-by-attributes");
             orderBys.push(column);
         }
         else {
             $(elem).addClass("glyphicon-sort");
             $(elem).removeClass("glyphicon-sort-by-attributes");
             orderBys.splice(orderBys.indexOf(column) != -1 ? orderBys.indexOf(column) : undefined, 1);
         }

         ajaxTableReload();
     });

     function addOrderBy() {
         var elem   = $(this);
         var column = $(elem).attr("id");
         //var column = $(elem).getAttribute("id");
         if ($(elem).hasClass("glyphicon-sort")) {
             $(elem).removeClass("glyphicon-sort");
             $(elem).addClass("glyphicon-sort-by-attributes");
             orderBys.push(column);
         }
         else {
             $(elem).addClass("glyphicon-sort");
             $(elem).removeClass("glyphicon-sort-by-attributes");
             orderBys.splice(orderBys.indexOf(column) != -1 ? orderBys.indexOf(column) : undefined, 1);
         }

         ajaxTableReload();
     }

    $('#searchForm').keypress(function(e) {
        if (e.which == '13') {
            e.preventDefault();
            ajaxTableReload();
        }
    });

    $('#searchBox').keypress(function(e) {
        if (e.which == '13') {
            e.preventDefault();
            ajaxTableReload();
        }
    });
});



// Function setCheckboxValues
(function ($) {

    $.fn.setCheckboxValues = function (formFieldName, checkboxFieldName) {

        var str = $('.' + checkboxFieldName + ':checked').map(function () {
            return this.value;
        }).get().join();

        $(this).attr('value', str);

        return this;
    };

}(jQuery));

// Function toggleEditMode
(function ($) {

    $.fn.toggleEditMode = function () {
        if ($(".editMode").is(":visible")) {
            $(".editMode").hide();
            $("#editComputer").text("Edit");
        }
        else {
            $(".editMode").show();
            $("#editComputer").text("View");
        }
        return this;
    };

}(jQuery));


// Function delete selected: Asks for confirmation to delete selected computers, then submits it to the deleteForm
(function ($) {
    $.fn.deleteSelected = function () {
        if (confirm("Are you sure you want to delete the selected computers?")) {
            $('#deleteForm input[name=selection]').setCheckboxValues('selection', 'cb');
            $('#deleteForm').submit();
        }
    };
}(jQuery));


//Event handling
//Onkeydown
$(document).keydown(function (e) {

    switch (e.keyCode) {
        //DEL key
        case 46:
            if ($(".editMode").is(":visible") && $(".cb:checked").length != 0) {
                $.fn.deleteSelected();
            }
            break;
        //E key (CTRL+E will switch to edit mode)
        case 69:
            if (e.ctrlKey) {
                $.fn.toggleEditMode();
            }
            break;
    }
});