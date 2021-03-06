// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.javascript.ift.lesson.editor

import com.intellij.application.options.CodeStyle
import com.intellij.javascript.ift.JsLessonsBundle
import com.intellij.javascript.ift.lesson.setLanguageLevel
import training.learn.interfaces.Module
import training.learn.js.textAfterOffset
import training.learn.js.textBeforeOffset
import training.learn.js.textOnLine
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonUtil
import training.learn.lesson.kimpl.parseLessonSample

class CodeEditingLesson(module: Module)
  : KLesson("Code Editing Tips and Tricks", JsLessonsBundle.message("js.editor.code.editing.tips.and.tricks.title"), module, "HTML") {

  val sample = parseLessonSample(""" 
        <!doctype html><html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Multiple selections</title>
</head>
<body>
<table>
  <tr>
      <td>Jackson</td>
     <td>Eve</td>
    <td>24</td>
  </tr>
  <tr>
    <td>First name</td>
    <td>Last name</td>
    <td>Age</td>
  </tr>
</table>
</body>
  </html>


        """.trimIndent())


  override val lessonContent: LessonContext.() -> Unit
    get() {
      return {
        setLanguageLevel()
        prepareRuntimeTask {
          CodeStyle.getSettings(project).AUTODETECT_INDENTS = false
        }
        prepareSample(sample)
        task("ReformatCode") {
          text(JsLessonsBundle.message("js.editor.code.editing.reformat.start", action(it)))
          trigger(it)
        }
        waitBeforeContinue(500)
        caret(232)
        task("EditorSelectWord") {
          text(JsLessonsBundle.message("js.editor.code.editing.select.word", action(it)))
          trigger(it) {
            val selectionEnd = editor.selectionModel.selectionEnd
            val selectionStart = editor.selectionModel.selectionStart
            val selectionEndLineNumber = editor.document.getLineNumber(selectionEnd)
            val selectionStartLineNumber = editor.document.getLineNumber(selectionStart)
            textBeforeOffset(selectionEnd, "</tr>") && textAfterOffset(selectionStart, "<tr>") && selectionEndLineNumber == 17 && selectionStartLineNumber == 13
          }
        }
        task("MoveStatementUp") {
          text(JsLessonsBundle.message("js.editor.code.editing.comment.delete.unselect.move.up", action("CommentByBlockComment"), action("EditorDeleteToWordStart"), action("EditorUnSelectWord"), action(it)))
          trigger(it)
        }
        task {
          text(JsLessonsBundle.message("js.editor.code.editing.multi.caret",
                                     code("td"), code("td"), action("SelectNextOccurrence"), code("td"), code("th"), action("EditorEscape")))
          stateCheck {
            textOnLine(9, "<th>") &&
            textOnLine(10, "<th>") &&
            textOnLine(11, "<th>")
          }
          trigger("EditorEscape")
        }
        task("CommentByLineComment") {
          text(
            JsLessonsBundle.message("js.editor.code.editing.duplicate.delete.comment", action("EditorDuplicate"), action("EditorDeleteLine"),
                                  action("CommentByLineComment")))
          trigger(it)
        }
        text(JsLessonsBundle.message("js.editor.code.editing.next", LessonUtil.rawEnter()))
      }
    }
  override val existedFile = "index.html"
}