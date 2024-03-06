/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["../resources/templates/**/*.{html,js}",
        "../resources/templates/**/**/*.{html,js}",
        "./node_modules/flowbite/**/*.js"],
    theme: {
        extend: {
            colors: {
                primary: '#3490dc',
            }
        }
    },
    plugins: [
        require('flowbite/plugin')
    ]
}