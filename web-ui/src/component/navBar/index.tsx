import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ThemeSwitch from "../themeSwitch";
import {useState, MouseEvent} from "react";
import Mapping from "../../page/mapping";
import {useNavigate} from "react-router-dom";

export default function NavBar(props: { setMode: any; theme: any}) {
    const navigate = useNavigate()
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar variant="dense">
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                        aria-controls={open ? 'basic-menu' : undefined}
                        aria-haspopup="true"
                        aria-expanded={open ? 'true' : undefined}
                        onClick={handleClick}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Menu
                        id="basic-menu"
                        anchorEl={anchorEl}
                        open={open}
                        onClose={handleClose}
                        MenuListProps={{
                            'aria-labelledby': 'basic-button',
                        }}
                    >
                        {Mapping.map((item, index) => (
                            <MenuItem
                                onClick={() => {
                                    handleClose();
                                    navigate(item.path)
                                }}
                                key={index}
                            >
                                {item.name}
                            </MenuItem>
                        ))}
                    </Menu>
                    <Typography variant="h6" color="inherit" component="div" sx={{ flexGrow: 1 }}>
                        Whatsit FileShare
                    </Typography>
                    <ThemeSwitch theme={props.theme} onChange={() => {
                        props.theme.palette.mode === 'dark' ? props.setMode('light'): props.setMode('dark')
                    }}/>
                </Toolbar>
            </AppBar>
        </Box>
    )
}
