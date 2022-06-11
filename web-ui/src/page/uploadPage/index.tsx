import Box from "@mui/material/Box";
import {styled} from '@mui/material/styles';
import {CloudUploadRounded, Add} from "@mui/icons-material";
import LinearProgress from '@mui/material/LinearProgress';
import Typography from '@mui/material/Typography';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';
import AlertTitle from '@mui/material/AlertTitle';
import {Button} from "@mui/material";
import {SyntheticEvent, useState} from "react";
import {DataGrid, GridActionsCellItem, GridColumns} from '@mui/x-data-grid';
import styles from './index.module.css'
import Constants from "../../constants";

const Input = styled('input')({
    display: 'none',
});

const chunkSize = 1024 * 1024 * 10

export default function UploadPage() {
    const [progress, setProgress] = useState(0);
    const [fileName, setFileName] = useState("");
    const [filesList, setFilesList] = useState([]);
    const [alertOpen, setAlertOpen] = useState(false);
    const [errorMsg, setErrorMsg] = useState("");

    const addFile = (event: any) => {
        const fileList = event.target.files
        if (fileList.length > 0) {
            for (let i = 0; i < fileList.length; i++) {
                const file = fileList[i]
                let data = {
                    id: Date.now(),
                    name: file.name,
                    size: (file.size/1024/1024).toString(),
                    status: 'added',
                    rawFile: file
                }
                // @ts-ignore
                setFilesList(prevState => [...prevState, data])
            }
        } else {
            setErrorMsg("nothing selected...")
            setAlertOpen(true)
        }
    }

    const upload = (params: any) => {
        setProgress(0)
        const file = params.row.rawFile
        setFileName(file.name)
        const totalChunks = Math.ceil(file.size / chunkSize)

        const perChunkProgress = 100 / totalChunks

        let chunk = 0
        while (chunk < totalChunks) {
            const offset = chunk * chunkSize
            let data = new FormData()
            data.append("file", file.slice(offset, offset + chunkSize))
            fetch(Constants.uploadURL, {
                method: 'POST',
                headers: {
                    "fileName": encodeURIComponent(file.name),
                    "totalChunk": totalChunks.toString(),
                    "chunk": chunk.toString(),
                },
                body: data,
            })
                .then(res => {
                    if (res.status === 200) {
                        res.json().then(async r => {
                            console.log(r)
                            setProgress(prevState => prevState + perChunkProgress)
                        })
                    }
                })
                .catch(err => {
                    console.error(err)
                    setErrorMsg("Connection issues...")
                    setAlertOpen(true)
                })
            chunk++
        }

    };

    const columns: GridColumns = [
        {
            field: 'name',
            headerName: 'File Name',
            width: 250
        },
        {
            field: 'size',
            headerName: 'Size(MB)',
            width: 100
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 100
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            width: 80,
            getActions: (params: any) => [
                <GridActionsCellItem label="upload" icon={<CloudUploadRounded/>} onClick={() => upload(params)}/>
            ]
        }
    ]

    const handleAlertClose = (event?: SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }

        setAlertOpen(false);
    };

    return (
        <Box className={styles.mainBox}>
            <Snackbar
                open={alertOpen}
                autoHideDuration={6000}
                onClose={handleAlertClose}
                anchorOrigin={{vertical: "top", horizontal: "left"}}
            >
                <Alert
                    severity="error"
                    onClose={handleAlertClose}
                    sx={{width: '100%'}}
                >
                    <AlertTitle>Error</AlertTitle>
                    {errorMsg}
                </Alert>
            </Snackbar>
            <Box className={styles.childBox}>
                <DataGrid
                    columns={columns}
                    rows={filesList}
                    // checkboxSelection
                    pageSize={5}
                    rowsPerPageOptions={[5]}
                    sx={{minWidth: 600, marginBottom: 1}}
                    showColumnRightBorder={true}
                />
                <label htmlFor="icon-button-file">
                    <Input
                        // accept="image/*"
                        id="icon-button-file"
                        type="file"
                        onInput={addFile}
                        multiple={true}
                    />
                    <Button
                        variant="contained"
                        color="secondary"
                        startIcon={<Add />}
                        aria-label="upload picture"
                        component="span"
                    >
                        Add File
                    </Button>
                </label>
                {
                    fileName.length > 0 ? (
                        <Box className={styles.progressBox} sx={{display: 'flex', alignItems: 'center'}}>
                            <Box sx={{minWidth: 100}}>
                                <Typography variant="body2" color="text.secondary">{fileName}</Typography>
                            </Box>
                            <Box sx={{width: '100%', mr: 1}}>
                                <LinearProgress variant="determinate" value={progress} color="secondary"/>
                            </Box>
                            <Box sx={{minWidth: 50}}>
                                <Typography variant="body2" color="text.secondary">{`${Math.round(
                                    progress,
                                )} %`}</Typography>
                            </Box>
                        </Box>
                    ) : null
                }
            </Box>
        </Box>
    )
}
