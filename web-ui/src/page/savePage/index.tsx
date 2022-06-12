import Box from "@mui/material/Box";
import {useEffect, useState} from "react";
import {DataGrid, GridColumns} from "@mui/x-data-grid";
import {IconButton, Link} from "@mui/material";
import {CloudDownloadRounded} from "@mui/icons-material";
import Constants from "../../constants";
import style from './index.module.css'

export default function SavePage() {
    const [fileList, setFileList] = useState([])
    useEffect(()=> {
        let ws = new WebSocket(Constants.downloadWSURL)
        ws.onopen = function () {
            console.log("Client connected websocket")
        }

        ws.onmessage = function (event) {
            if (event.data === 'deleteAll') {
                setFileList([])
                return
            }
            console.log(event.data)
            let data = JSON.parse(event.data)
            data["id"] = Date.now()
            data["fileSize"] = data["fileSize"]/1024/1024
            data["fileLastModified"] = new Date(data["fileLastModified"]).toLocaleString()
            data["uploadTime"] = new Date().toLocaleString()
            // @ts-ignore
            setFileList(prevState => [...prevState, data])
        }

        ws.onclose = function () {
            console.log("ws connection closed!")
        }
        return (() => {
            ws.close()
        })
    }, [])

    const columns: GridColumns = [
        {
            field: 'fileName',
            headerName: 'File Name',
            width: 250
        },
        {
            field: 'fileSize',
            headerName: 'Size(MB)',
            width: 100
        },
        {
            field: 'fileLastModified',
            headerName: 'Last Modified',
            width: 200
        },
        {
            field: 'uploadTime',
            headerName: 'Upload Time',
            width: 200
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            width: 80,
            getActions: (params: any) => [
                <IconButton>
                    <Link
                        underline="none"
                        href={Constants.downloadURL + "/" + encodeURIComponent(params.row.fileName)}
                    >
                        <CloudDownloadRounded />
                    </Link>
                </IconButton>
            ]
        }
    ]

    return (
        <Box className={style.mainBox}>
            <Box className={style.childBox}>
                <DataGrid
                    columns={columns}
                    rows={fileList}
                    // checkboxSelection
                    pageSize={5}
                    rowsPerPageOptions={[5]}
                    sx={{minWidth: 1000, marginBottom: 1}}
                    showColumnRightBorder={true}
                />
            </Box>
        </Box>
    )
}
