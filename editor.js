var editor = ace.edit("editor");
var defValue = "package test; public class Test {public static void main(String[] args) throws Exception {System.out.print(\"Hello world\");}}"
var jsbOpts = {
	indent_size : 2
};

var lang = "java"
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
		value = "package test; public class Test {public static void main(String[] args) throws Exception {System.out.print(\"Hello world\");}}";
		lang = "java"
		break;
	case "python":
		value = "print('Hello World')"
		lang = "python"
		break;
	}

	syncEditor(value);
}

function submit() {
	var source = JSON.stringify(editor.getSession().getValue());
	var input = "{ \"source\":"  +  source  + "," + "\"lang\":" + "\"" + lang + "\"" + "}";
	console.log(input)
	$.post("http://localhost:8090/compile", input, function(data) {
		console.log(data)
		$('#outputarea').val(data)
		
	});

	/*$.get("http://localhost:8090/compile", function(data){
		console.log(data)
	});*/
}
