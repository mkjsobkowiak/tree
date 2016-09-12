$(document).ready(function () {
    /*
     Tree creator with listeners on create, rename and delete
     */
    $('#container').jstree({
        "core": {
            "animation": 0,
            "check_callback": true,
            "themes": {"stripes": true},
            "strings": {
                "New node": ""
            },
            "data": {
                "url": function (node) {
                    var nodeId = "";
                    var url = ""
                    if (node.id === "#") {
                        url = "node/root";
                    } else {
                        nodeId = node.id;
                        url = "node/" + nodeId + "/children";
                    }

                    return url;
                },
                "success": function (new_data) {
                    return new_data;
                }
            }
        },
        "types": {
            "root": {
                "icon": "image/tree-icon.png",
                "valid_children": ["default"]
            },
            "default": {
                "valid_children": ["default", "file"]
            }
        },
        "plugins": [
            "contextmenu", "themes"
        ],
        "contextmenu": {
            "items": function ($node) {
                var tree = $("#container").jstree(true);
                return {
                    "Rename": {
                        "label": "Rename",
                        "action": function (obj) {
                            tree.edit($node);
                        }
                    },
                    "Create": {
                        "label": "Create",
                        "action": function (obj) {
                            $node = tree.create_node($node);
                            tree.edit($node);
                        }
                    },
                    "Remove": {
                        "label": "Remove",
                        "action": function (obj) {
                            tree.delete_node($node);
                        }
                    }
                };
            }
        }
    }).bind('create_node.jstree', function (node, ref) {
        console.log("create", node, ref);
        var node = ref.node;
        node.name = node.text;
        node.parentId = ref.node.parent;
        $.ajax({
            url: "/node",
            data: JSON.stringify({
                parentId: ref.parent
            }),
            type: 'POST',
            contentType: 'application/json'
        })
            .done(function (data) {
                $("#container").jstree(true).set_id(ref.node, data.id);
                console.log("succes", data);
            }).fail(function (data) {
                console.log("error", data);
                $('#container').jstree("refresh");
            });
    }).bind('rename_node.jstree', function (node, ref) {
        if (!isNumeric(ref.text) && ref.text !== "") {
            $("#container").jstree('rename_node', [ref.node.id], "0.0");
        } else {
            $.ajax({
                url: "/node/" + ref.node.id,
                data: JSON.stringify({
                    parentId: ref.node.parent,
                    text: ref.node.text
                }),
                type: 'PUT',
                contentType: 'application/json'
            })
                .done(function (data) {
                    console.log("succes rename", data);
                    $('#container').jstree("refresh");
                }).fail(function (data) {
                    console.log("error rename", data);
                    $('#container').jstree("refresh");
                });
        }
    }).bind('delete_node.jstree', function (node, ref) {
        $.ajax({
            url: "/node/" + ref.node.id,
            data: null,
            type: 'DELETE',
            contentType: 'application/json'
        })
            .done(function (data) {
                console.log("succes delete", data);
            }).fail(function (data) {
                console.log("error delete", data);
                $('#container').jstree("refresh");
            });
    });
});

/*
 Function which return true if given object is number
 */
function isNumeric(obj) {
    return !isNaN(obj - parseFloat(obj));
}