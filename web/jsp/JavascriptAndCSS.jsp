<script src="../js/jquery-2.1.4.js"></script>
<script src="../js/jquery-2.1.4.js"></script>
<script src="../js/jquery-ui.js"></script>
<script src="../js/jquery.jtable.js"></script>

<link rel="stylesheet" type="text/css" href="../css/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui.blitzer.css" />
<link rel="stylesheet" type="text/css" href="../css/jtable/jtable_jqueryui.css" />

<style>
body, td, th {
font-size:12px;
}
</style>
<script>
var blurTimer;
var blurTimeAbandoned = 200;

function createMenu()
{
    var $menuContainer = $('#menuContainer');
    var pos = $menuContainer.position();
    var height = $menuContainer.outerHeight();
    var width = $menuContainer.outerWidth();

    var $menu = $('<ul />')
        .css({ position: 'absolute', 'z-index' : 300, left: pos.left + 'px', top: (pos.top + height + 2) + 'px', width : width + 'px' });
    $('<li />').attr({url : 'Student'}).html('Student').appendTo($menu);
    $('<li />').attr({url : 'City'}).html('City').appendTo($menu);
    $('<li />').attr({url : 'Course'}).html('Course').appendTo($menu);
    $('<li />').attr({url : 'DatabaseSetup'}).html('Database Setup').appendTo($menu);

    $menu.appendTo($menuContainer);
    $menu.menu(
    {
        select: function (event, ui) {
            var item = ui.item;
            var url = item.attr('url');
            location.href = url;
        },

        focus: function (event, ui) {
            clearTimeout(blurTimer);
        },

        blur: function (event, ui) {
            blurTimer = setTimeout(function() {
                $menu.remove();
            }, blurTimeAbandoned);
        }
    });
}

$(document).ready(function () {
    var opts = $.hik.jtable.prototype.options;
    opts.defaultDateFormat = "dd/mm/yy";
    opts.openChildAsAccordion = true;
    opts.jqueryuiTheme = true;

    var $table = $('<table />')
        .attr({ width : '100%', cellPadding : '5'})
        .addClass('ui-widget-header ui-widget');
    var $tr = $('<tr />').appendTo($table);
    var $td = $('<td />').attr({align : 'center'}).css({width : '25px'}).appendTo($tr);
    var $a = $('<a href="Home" />');
    $a.appendTo($td);
    var $img = $('<img src="../css/img/Home.png" title="Home" />');
    $img.appendTo($a);
    var $td = $('<td />')
        .html('Menu')
        .click(createMenu)
        .attr({id : 'menuContainer'})
        .css({width : '125px', cursor : 'pointer'})
        .appendTo($tr);
    var $td = $('<td />')
        .html('Spring JQuery')
        .attr({align : 'center'})
        .css({'font-size': '1.1em'})
        .appendTo($tr);
    $table.appendTo('body');

    $div = $('<div />').attr({ id : 'JTable'}).appendTo('body');

});
</script>