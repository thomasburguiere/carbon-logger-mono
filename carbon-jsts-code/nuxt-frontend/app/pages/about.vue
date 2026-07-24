<template>
  <Header title="about" :enable-back="true"/>

  <main>
    <h2>About Carbon Page</h2>

    <UCard>
      <h2>PoC Carbon Model integration</h2>

      <p>Here's my current CarbonMeasurement:</p>
      <pre><code>
          {
            "id": {{measurement.id}},
            "co2Kg": {{measurement.co2Kg}},
            "dt": {{measurement.dt}}
          }
          </code></pre>

      <p>Check out <a
          href="https://docs.microsoft.com/en-us/microsoft-edge/progressive-web-apps-chromium/how-to/handle-files">these
        docs</a> to learn more about the advanced features that you can use in your PWA</p>
    </UCard>
  </main>
</template>
<script setup lang="ts">
import Header from "~/components/Header.vue";
import {CarbonMeasurementBuilder} from "carbonlog-model";

const measurement = new CarbonMeasurementBuilder().co2Kg(42.0).build()


</script>
<style>
@media(min-width: 1000px) {
  sl-card {
    max-width: 70vw;
  }
}
</style>
