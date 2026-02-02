import { LitElement, html } from 'lit';
import { customElement } from 'lit/decorators.js';

// You can also import styles from another file
// if you prefer to keep your CSS seperate from your component
import { styles } from './about-styles';

import { styles as sharedStyles } from '../../styles/shared-styles'

import '@shoelace-style/shoelace/dist/components/card/card.js';
import { CarbonMeasurement, CarbonMeasurementBuilder } from 'carbonlog-model';

@customElement('app-about')
export class AppAbout extends LitElement {
  static styles = [
    sharedStyles,
    styles
  ]

  measurement: CarbonMeasurement = new CarbonMeasurementBuilder().co2Kg(42.0).build()

  render() {
    return html`
      <app-header ?enableBack="${true}"></app-header>

      <main>
        <h2>About Carbon Page</h2>

        <sl-card>
          <h2>PoC Carbon Model integration</h2>

          <p>Here's my current CarbonMeasurement:</p>
          <pre><code>
          {
            "id": ${this.measurement.id},
            "co2Kg": ${this.measurement.co2Kg},
            "dt": ${this.measurement.dt}
          }
          </code></pre>

          <p>Check out <a
              href="https://docs.microsoft.com/en-us/microsoft-edge/progressive-web-apps-chromium/how-to/handle-files">these
              docs</a> to learn more about the advanced features that you can use in your PWA</p>
        </sl-card>
      </main>
    `;
  }
}
