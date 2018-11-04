var editor = ace.edit("editor");
var defValue = "public class SpringTest {public static void main(String[] args) throws Exception {System.out.print(\"Hello world\");}}"
var jsbOpts = {
	indent_size : 2
};

editor.setTheme("ace/theme/monokai");
editor.resize();
editor.session.setMode("ace/mode/java");

syncEditor(defValue);

// Main Logic

// Functions
function syncEditor(value) {
	editor.getSession().setValue(value);
	setTimeout(formatCode, 500); // Format sample Java after 1 second.
}

function formatCode() {
	var session = editor.getSession();
	session.setValue(js_beautify(session.getValue(), jsbOpts));
}

function updateEditor(mode) {
	console.log("in update editor");
	editor.session.setMode("ace/mode/" + mode);
	value = ""
	switch (mode) {
	case "java":
		value = "package com.hack; public class Test {public static void main(String[] args) throws Exception {System.out.print(\"Hello world\");}}";
		break;
	case "javascript":
		value = "function foo(items) { var x = \"All this is syntax highlighted\"; return x;}";
		break;
	case "php": value="<?php echo \"Write something here...\"; ?>"; break;
	
	case "csharp": value="namespace HelloWorld { class Hello { static void Main(string[] args) { System.Console.WriteLine(\"Hello World!\");}}}"; break;
	}

	syncEditor(value);
}
