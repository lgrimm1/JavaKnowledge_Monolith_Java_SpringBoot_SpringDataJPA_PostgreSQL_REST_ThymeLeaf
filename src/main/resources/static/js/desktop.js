window.onload = function() {
    document.getElementById("titles_list").selectedIndex = 0;
    var contentTextArea = document.getElementById("txt_content");
    contentTextArea.focus();
    contentTextArea.setSelectionRange(contentTextArea.value.length, contentTextArea.value.length);
}

function content_to_clipboard(element) {
    element.select();
    document.execCommand('copy');
}

function element_to_full_size(element) {
    element.style.height = "";
    element.style.height = element.scrollHeight + "px";
}
