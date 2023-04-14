window.onload = function() {
    document.getElementById("titles_list").selectedIndex = 0;
}

function content_to_clipboard(id) {
    document.getElementById(id).select();
    document.execCommand('copy');
}

function element_to_full_size(element) {
    element.style.height = "";
    element.style.height = element.scrollHeight + "px";
}
