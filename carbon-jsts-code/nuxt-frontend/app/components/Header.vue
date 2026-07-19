<template>
  <header :enable-back="props.enableBack" :title="props.title">
<!--    <div id="back-button-block">-->
<!--      {{this.enableBack ? `<sl-button size="small" href="${resolveRouterPath()}">`}}}-->
<!--      Back-->
<!--    </sl-button>` : null}-->

      <h1>{{ props.title}}</h1>
<!--    </div>-->
  </header>

</template>
<script lang="ts" setup>
const props = defineProps<{title:string, enableBack: boolean}>()
</script>
