    window.onload = function() {
        document.getElementById("titles_list").selectedIndex = 0;
        var contentTextArea = document.getElementById("txt_content");
        contentTextArea.focus();
        contentTextArea.setSelectionRange(contentTextArea.value.length, contentTextArea.value.length);
    }
