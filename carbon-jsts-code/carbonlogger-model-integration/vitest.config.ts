import { defineConfig } from 'vitest/config';

export default defineConfig({
    test: {
        environment: 'node', // 'node' for server, 'jsdom' for client
        coverage: {
            provider: 'v8',
            reportsDirectory: './coverage',
            reporter: ['cobertura', 'html', 'text-summary'],
            exclude: ['**/index.ts', 'dist', '*config.*s', '**/*spec.ts', '**/src/constants/**'],
        },
    },
});
