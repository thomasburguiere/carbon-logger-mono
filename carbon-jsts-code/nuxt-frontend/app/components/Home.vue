<template>
  <Header title="carbon" :enable-back="false"/>
  <main>
    <div id="welcomeBar">
      <UCard id="welcomeCard">
        <div slot="header">
          <h2>{{ message }}</h2>
        </div>

        <p>
          For more information on the PWABuilder pwa-starter, check out the
          <a href="https://docs.pwabuilder.com/#/starter/quick-start">
            documentation</a>.
        </p>

        <p id="mainInfo">
          Welcome to the
          <a href="https://pwabuilder.com">PWABuilder</a>
          pwa-starter! Be sure to head back to
          <a href="https://pwabuilder.com">PWABuilder</a>
          when you are ready to ship this PWA to the Microsoft Store, Google Play
          and the Apple App Store!
        </p>

        <template v-if="hasShare">
          <UButton slot="footer" variant="default" @click="share">
            <UIcon slot="prefix" name="share"></UIcon>
            Share this Starter!
          </UButton>
        </template>
      </UCard>

      <UCard id="infoCard">
        <h2>Technology Used</h2>

        <ul>
          <li>
            <a href="https://www.typescriptlang.org/">TypeScript</a>
          </li>

          <li>
            <a href="https://kotlinlang.org/docs/multiplatform.html">Kotlin Multiplatform</a>
          </li>

          <li>
            <a href="https://lit.dev">Lit</a>
          </li>

          <li>
            <a href="https://shoelace.style/">Shoelace</a>
          </li>

          <li>
            <a href="https://github.com/thepassle/app-tools/blob/master/router/README.md"
            >App Tools Router</a>
          </li>
        </ul>
      </UCard>


      <UButton href="/about">Navigate to About Carbon</UButton>
    </div>
  </main>
</template>

<script lang="ts" setup>
import Header from "~/components/Header.vue"

const message = "Welcome";

const hasShare = !!navigator && 'share' in navigator;

const share = () => {
  if ((navigator as any).share) {
    (navigator as any).share({
      title: 'PWABuilder pwa-starter',
      text: 'Check out the PWABuilder pwa-starter!',
      url: 'https://github.com/pwa-builder/pwa-starter',
    });
  }
}

</script>

<style>
#welcomeBar {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

#welcomeCard,
#infoCard {
  padding: 18px;
  padding-top: 0px;
}

sl-card::part(footer) {
  display: flex;
  justify-content: flex-end;
}

@media (min-width: 750px) {
  sl-card {
    width: 70vw;
  }
}


@media (horizontal-viewport-segments: 2) {
  #welcomeBar {
    flex-direction: row;
    align-items: flex-start;
    justify-content: space-between;
  }

  #welcomeCard {
    margin-right: 64px;
  }
}
</style>
