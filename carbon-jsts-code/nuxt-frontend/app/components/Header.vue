<template>
  <header :enable-back="props.enableBack" :title="props.title">
    <div id="back-button-block">

      <UButton v-if="enableBack" @click="$router.back()">Back</UButton>

      <h1>{{ props.title}}</h1>
    </div>
  </header>

</template>
<script lang="ts" setup>
const props = defineProps<{title:string, enableBack: boolean}>()

</script>
