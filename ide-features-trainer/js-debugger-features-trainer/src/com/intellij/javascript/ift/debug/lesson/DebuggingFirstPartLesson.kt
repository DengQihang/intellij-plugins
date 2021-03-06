// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.javascript.ift.debug.lesson

import com.intellij.icons.AllIcons
import com.intellij.javascript.debugger.JSDebuggerBundle
import com.intellij.javascript.ift.debug.JsDebugLessonsBundle
import com.intellij.javascript.ift.debug.lesson.BeforeDebuggingLesson.Companion.jsDebuggerSample
import com.intellij.javascript.ift.debug.lineContainsBreakpoint
import com.intellij.javascript.ift.debug.setLanguageLevel
import com.intellij.ui.UIBundle
import com.intellij.xdebugger.XDebuggerBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonUtil

class DebuggingFirstPartLesson(module: Module)
  : KLesson("Debugging Code. Part I", JsDebugLessonsBundle.message("js.debugger.part.1.title"), module, "JavaScript") {

  override val lessonContent: LessonContext.() -> Unit
    get() {
      return {
        setLanguageLevel()
        prepareSample(jsDebuggerSample)
        task("Run") {
          text(JsDebugLessonsBundle.message("js.debugger.part.1.start", icon(AllIcons.RunConfigurations.TestState.Run)))
          trigger(it)
        }

        task {
          text(JsDebugLessonsBundle.message("js.debugger.part.1.gutter", code("10"), code("-20"), code("Different!"), code("Equal!")))
          stateCheck {
            lineContainsBreakpoint(1)
          }
        }

        task("Debug") {
          text(JsDebugLessonsBundle.message("js.debugger.part.1.set.breakpoint", icon(AllIcons.Actions.StartDebugger), action(it)))
          trigger(it)
        }

        task {
          text(JsDebugLessonsBundle.message("js.debugger.part.1.tool.window", UIBundle.message("tool.window.name.debug"), strong(XDebuggerBundle.message("xdebugger.default.content.title")), strong(XDebuggerBundle.message("debugger.session.tab.variables.title")), strong(XDebuggerBundle.message("debugger.session.tab.frames.title")), strong(XDebuggerBundle.message("debugger.session.tab.console.content.name"))))
          stateCheck {
            val text = focusOwner.toString()
            text.contains("Terminal") 
          }
        }

        task {
          text(JsDebugLessonsBundle.message("js.debugger.part.1.scripts.tab",
                                            strong(XDebuggerBundle.message("debugger.session.tab.console.content.name")),
                                            strong(JSDebuggerBundle.message("js.console.debug.name")),
                                            strong(JSDebuggerBundle.message("js.scripts.tab.title"))))
          stateCheck {
            focusOwner.toString().contains("treeStructure.SimpleTree")
          }
        }
        text(JsDebugLessonsBundle.message("js.debugger.part.1.next", strong(JSDebuggerBundle.message("js.scripts.tab.title")),
                                          LessonUtil.rawEnter()))
      }
    }
  override val existedFile = "debugging.js"
}


