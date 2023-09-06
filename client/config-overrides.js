const path = require('path');

module.exports = function override(config, env) {
    // Add or modify Webpack configurations here
    if (!config.resolve) {
        config.resolve = {};
    }

    if (!config.resolve.fallback) {
        config.resolve.fallback = {};
    }

    config.resolve.fallback = {
        ...config.resolve.fallback,
        "fs": false,
        "path": require.resolve("path-browserify"),
        "os": require.resolve("os-browserify/browser")
    };
    
    config.target = 'electron-renderer';


    return config;
};
