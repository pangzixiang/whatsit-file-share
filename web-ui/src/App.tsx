import React, {useMemo, useState} from 'react';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import NavBar from "./component/navBar";
import Mapping from "./page/mapping";
import './App.css';

function App() {
    const [mode, setMode] = useState<'light' | 'dark'>('light')

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode: mode,
                    ...(mode === 'light')?{
                        primary: {
                            main: "#00bfa5"
                        },
                        secondary: {
                            main: "#455a64",
                        },
                        // divider: amber[200],
                        background: {
                            default: "#5df2d6",
                            paper: "#00bfa5",
                        },
                        text: {
                            primary: "#5c6bc0",
                            secondary: "#7e57c2",
                        },
                    }: {
                        // primary: deepOrange,
                        // divider: deepOrange[700],
                        primary: {
                            main: "#00bfa5"
                        },
                        secondary: {
                            main: "#455a64"
                        },
                        background: {
                            default: "#008e76",
                            paper: "#00bfa5",
                        },
                        text: {
                            primary: '#283593',
                            secondary: "#4527a0",
                        },
                    }
                },
            }),
        [mode])
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <div className="App">
                <BrowserRouter>
                    <NavBar setMode={setMode} theme={theme}/>
                    <Routes>
                        {
                            Mapping.map((item, index) => (
                                <Route path={item.path} key={index} element={<item.element />} />
                            ))
                        }
                    </Routes>
                </BrowserRouter>
            </div>
        </ThemeProvider>
    );
}

export default App;
