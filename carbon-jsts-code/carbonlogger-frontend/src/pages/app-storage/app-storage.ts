import { LitElement, html } from 'lit';
import { customElement, state } from 'lit/decorators.js';

import { styles as sharedStyles } from '../../styles/shared-styles';
import { styles } from '../app-about/about-styles';

@customElement('app-storage')
export class AppStorage extends LitElement {
  static styles = [sharedStyles, styles];

  @state() private db: IDBDatabase | null = null;
  @state() private message: string = '';

  // Database configuration
  private readonly DB_NAME = 'MyPwaDatabase';
  private readonly DB_VERSION = 2;
  private readonly STORE_NAME = 'dataStore';

  connectedCallback() {
    super.connectedCallback();
    this.initDatabase();
  }

  private async initDatabase() {
    // Request persistent storage
    if (navigator.storage && navigator.storage.persist) {
      const persistent = await navigator.storage.persist();
      this.message = persistent
        ? 'Persistent storage granted.'
        : 'Persistent storage not granted.';
    }

    // Open IndexedDB
    const request = indexedDB.open(this.DB_NAME, this.DB_VERSION);

    request.onerror = () => {
      this.message = 'Database error: ' + request.error?.message;
    };

    request.onsuccess = () => {
      this.db = request.result;
      this.message = 'Database opened successfully.';
    };

    // Create object store if it doesn't exist
    request.onupgradeneeded = () => {
      const db = request.result;
      if (!db.objectStoreNames.contains(this.STORE_NAME)) {
        db.createObjectStore(this.STORE_NAME, {
          keyPath: 'id',
          autoIncrement: true,
        });
        this.message = 'Object store created.';
      }
    };
  }

  // private readData() {
  //
  // }

  private storeData() {
    if (!this.db) {
      this.message = 'Database not ready.';
      return;
    }

    const transaction = this.db.transaction([this.STORE_NAME], 'readwrite');
    const store = transaction.objectStore(this.STORE_NAME);

    const data = {
      value: `Stored at ${new Date().toISOString()}`,
      timestamp: Date.now()
    };

    const request = store.add(data);

    request.onsuccess = () => {
      this.message = `Data stored with ID: ${request.result}`;
    };

    request.onerror = () => {
      this.message = 'Error storing data: ' + request.error?.message;
    };
  }

  render() {
    return html` <app-header ?enableBack="${true}"></app-header>

      <main>
        <h2>Storage Page</h2>

        <p>DB Status: ${this.message}</p>
        <button @click=${this.storeData} ?disabled=${!this.db}>
          Store Data in IndexedDB
        </button>
      </main>`;
  }
}


