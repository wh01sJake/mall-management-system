<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {onBeforeUnmount, ref, shallowRef, onMounted, watch} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {ElMessage} from "element-plus";

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('<p></p>')
//初始值，用于做数据回显功能
const props = defineProps({
  initValue: {
    type: String,
    default: ''
  }
})
//主要用于父组件接收wangeditor实时编辑的内容
const emit = defineEmits(['getEditorContent'])

//监听父组件传递过来值的变化，用于回显
watch(() => props.initValue, value => {
  console.log(value)
  valueHtml.value = value ?? ''
})
//监听富文本编辑器内容变化，通知父组件得到数据
watch(valueHtml, (content) => {
  emit('getEditorContent', content)
})

const toolbarConfig = {}
//图片上传
import {useTokenStore} from '@/store/token.js'

const tokenStore = useTokenStore();
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      fieldName: 'file',
      server: '/api/upload',
      // 自定义增加 http  header
      headers: {
        Authorization: tokenStore.token,
      },
      customInsert(res, insertFn) {
        // res 即服务端的返回结果
        console.log(res)
        // 从 res 中找到 url alt href ，然后插入图片
        if (res.code == 0) {
          insertFn('/api/pic/' + res.data)
        } else {
          ElMessage.error('插入图片异常')
        }
      },
    }
  }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}
</script>

<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
    />
    <Editor
        style="height: 500px; overflow-y: hidden;"
        v-model="valueHtml"
        :defaultConfig="editorConfig"
        @onCreated="handleCreated"
    />
  </div>
</template>

<style scoped>

</style>