/** @type {import('tailwindcss').Config} */
const {join} = require('path');
module.exports = {
    content: [
        join(__dirname, '/**/*.{js,ts,html}'),
        join(__dirname, '../../libs/carbonlog-frontend-lib/**/*.{js,ts,html}'),
        join(__dirname, '../../libs/shared-ui/**/*.{js,ts,html}'),
        "./node_modules/flowbite/**/*.js"
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('flowbite/plugin')
    ]
}
