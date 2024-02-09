/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["../resources/templates/**/*.{html,js}",
        "./node_modules/flowbite/**/*.js"], // it will be explained later
    theme: {
        extend: {},
    },
    plugins: [
        require('flowbite/plugin')
    ]
}